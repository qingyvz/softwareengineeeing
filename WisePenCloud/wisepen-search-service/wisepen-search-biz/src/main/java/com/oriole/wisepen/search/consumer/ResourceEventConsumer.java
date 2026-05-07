package com.oriole.wisepen.search.consumer;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oriole.wisepen.common.core.context.SecurityContextHolder;
import com.oriole.wisepen.common.core.domain.R;
import com.oriole.wisepen.common.core.domain.enums.IdentityType;
import com.oriole.wisepen.resource.domain.dto.ResourceInfoGetReqDTO;
import com.oriole.wisepen.resource.domain.dto.res.ResourceItemResponse;
import com.oriole.wisepen.search.domain.entity.SearchIndexEntity;
import com.oriole.wisepen.search.domain.mq.AclRecalculateMessage;
import com.oriole.wisepen.search.domain.mq.ResourceDeletedMessage;
import com.oriole.wisepen.search.feign.SearchSpecificResourceClient;
import com.oriole.wisepen.search.service.ISearchSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 资源变更/权限重算/硬删除 事件消费者
 * 严格遵守“Thin Event + RPC Pull”模式，并利用 common 基建进行上下文加固
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ResourceEventConsumer {

    private final ISearchSyncService searchSyncService;
    private final ObjectMapper objectMapper;
    private final SearchSpecificResourceClient searchResourceFallbackClient;

    /**
     * 由配置类注入的调度线程池，用于处理延迟反查逻辑
     */
    private final ScheduledExecutorService scheduler;

    /**
     * 系统管理员虚拟 ID 与身份标识
     */
    private static final Long SYSTEM_USER_ID = 0L;

    /**
     * 1. 监听：权限重新计算事件
     */
    @KafkaListener(topics = "wisepen-resource-acl-recalc-topic", groupId = "wisepen-search-group")
    public void onAclRecalculate(ConsumerRecord<String, String> record) {
        AclRecalculateMessage msg;
        try {
            msg = objectMapper.readValue(record.value(), AclRecalculateMessage.class);
        } catch (Exception e) {
            log.error("MQ 消息反序列化失败（毒消息），offset: {}, value: {}", record.offset(), record.value());
            return;
        }

        String resourceId = msg.getResourceId();
        if (StrUtil.isBlank(resourceId)) {
            return;
        }

        log.info("收到资源 [权限重算] 通知，3秒后延迟反查。resourceId: {}", resourceId);

        // 🚨 解决时间差陷阱：通过异步线程池执行延迟任务
        scheduler.schedule(() -> {
            try {
                // 💡 架构关键：在异步线程手动填充安全上下文，模拟管理员身份
                SecurityContextHolder.setUserId(SYSTEM_USER_ID);
                SecurityContextHolder.setIdentityType(IdentityType.ADMIN.getCode());

                fetchAndUpsertResourceMetaData(resourceId);
            } catch (Exception e) {
                log.error("执行延迟反查任务发生异常, resourceId: {}", resourceId, e);
            } finally {
                // 💡 架构关键：任务结束必须清理 ThreadLocal，防止线程污染
                SecurityContextHolder.remove();
            }
        }, 3, TimeUnit.SECONDS);
    }

    /**
     * 2. 监听：资源硬删除事件
     */
    @KafkaListener(topics = "wisepen-resource-physical-destroy-topic", groupId = "wisepen-search-group")
    public void onResourceDestroy(ConsumerRecord<String, String> record) {
        try {
            ResourceDeletedMessage msg = objectMapper.readValue(record.value(), ResourceDeletedMessage.class);
            Map<String, List<String>> typedResourceIds = msg.getTypedResourceIds();

            if (CollUtil.isEmpty(typedResourceIds)) {
                return;
            }

            // 遍历所有格式 (如 pdf, note) 下的被删资源 ID 列表，生成 ES ID 并物理删除
            typedResourceIds.forEach((format, idList) -> {
                if (CollUtil.isNotEmpty(idList)) {
                    idList.forEach(resourceId -> {
                        String esId = SearchIndexEntity.generateEsId(format, resourceId);
                        searchSyncService.deleteIndex(esId);
                        log.info("资源硬删除已同步至 ES。esId: {}", esId);
                    });
                }
            });

        } catch (Exception e) {
            log.error("处理资源物理删除 MQ 发生严重异常, offset: {}", record.offset(), e);
        }
    }

    /**
     * ======================== 核心业务处理方法 ========================
     */

    /**
     * 执行 Feign 反查并局部更新(Upsert) 到 ES
     * 职责：仅拉取元数据与权限，不触碰 content 字段
     */
    private void fetchAndUpsertResourceMetaData(String resourceId) {
        // 1. 组装请求参数
        ResourceInfoGetReqDTO reqDTO = new ResourceInfoGetReqDTO();
        reqDTO.setResourceId(resourceId);
        reqDTO.setUserId(SYSTEM_USER_ID);

        // 2. 发起 Feign 调用（RPC Pull）
        R<ResourceItemResponse> rpcResponse = searchResourceFallbackClient.getResourceInfo(reqDTO);

        if (rpcResponse == null  || rpcResponse.getData() == null) {
            log.warn("RPC 反查资源详情失败，丢弃本次同步。resourceId: {}", resourceId);
            return;
        }

        ResourceItemResponse detail = rpcResponse.getData();

        // 3. 像素级提取 ACL 权限名单
        List<String> allowedUsers = Optional.ofNullable(detail.getSpecifiedUsersGrantedActions())
                .map(m -> new ArrayList<>(m.keySet()))
                .orElse(new ArrayList<>());

        // 💡 扩展位：此处可根据后续业务需求，同样提取 detail.getSpecifiedGroups() 到 allowedGroups

        // 4. 提取资源格式并校验
        String actualFormat = "unknown";
        if (detail.getResourceType() != null) {
            actualFormat = detail.getResourceType().getExtension();
        }

        // 5. 组装 ES 局部更新实体
        SearchIndexEntity entity = SearchIndexEntity.builder()
                .id(SearchIndexEntity.generateEsId(actualFormat, resourceId))
                .resourceId(resourceId)
                .resourceType(actualFormat)
                .title(detail.getResourceName()) // 将资源名作为 ES 标题
                .allowedUsers(allowedUsers)
                .updateTime(LocalDateTime.now())
                .build();

        // 6. 执行 Upsert (有则局部更新，无则全量创建)
        // 该方法在底层通过 ElasticsearchOperations.update 保证了不会覆盖 content 字段
        searchSyncService.upsertIndexMetaData(entity);

        log.debug("资源元数据与权限已成功 Upsert。esId: {}", entity.getId());
    }
}