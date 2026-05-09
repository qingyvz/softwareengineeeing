package com.oriole.wisepen.search.consumer;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oriole.wisepen.common.core.domain.R;
import com.oriole.wisepen.common.core.domain.enums.IdentityType;
import com.oriole.wisepen.common.core.domain.enums.ResultCode;
import com.oriole.wisepen.common.core.context.SecurityContextHolder;
import com.oriole.wisepen.document.api.domain.mq.DocumentReadyMessage;
import com.oriole.wisepen.resource.domain.dto.ResourceInfoGetReqDTO;
import com.oriole.wisepen.resource.domain.dto.res.ResourceItemResponse;
import com.oriole.wisepen.resource.enums.ResourceType;
import com.oriole.wisepen.resource.feign.RemoteResourceService;
import com.oriole.wisepen.search.domain.entity.SearchIndexEntity;
import com.oriole.wisepen.search.service.ISearchSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class DocumentReadyConsumer {

    private final ObjectMapper objectMapper;
    private final ISearchSyncService searchSyncService;
    private final RemoteResourceService remoteResourceService;

    // 系统管理员虚拟 ID
    private static final Long SYSTEM_USER_ID = 0L;

    @KafkaListener(topics = "wisepen_document_ready_topic", groupId = "wisepen-search-group")
    public void onMessage(ConsumerRecord<String, String> record) {
        String messageValue = record.value();
        if (StrUtil.isBlank(messageValue)) return;

        try {
            // 💡 架构关键：在异步线程模拟系统管理员身份，防止 Feign 拦截器和下游权限判定失败
            SecurityContextHolder.setUserId(SYSTEM_USER_ID);
            SecurityContextHolder.setIdentityType(IdentityType.ADMIN.getCode());

            // 1. 解析原始 MQ 契约
            DocumentReadyMessage message;
            try {
                message = objectMapper.readValue(messageValue, DocumentReadyMessage.class);
            } catch (Exception e) {
                log.error("【格式错误】Kafka 消息 JSON 解析失败，跳过该消息！Offset: {}", record.offset());
                return;
            }

            String resourceId = message.getResourceId();
            String rawContent = message.getContent();

            log.info("【搜索服务】开始处理文档就绪事件, resourceId: {}, content_length: {}", resourceId, StrUtil.length(rawContent));
            if (StrUtil.isBlank(resourceId)) return;

            // 2. 预初始化：直接利用 DTO 数据填充基础信息（降级方案）
            // 哪怕 Feign 调用失败，我们至少能把正文存进去
            String actualResourceType = ResourceType.UNKNOWN.name();
            String resourceName = "未命名文档 (" + resourceId + ")";
            List<String> tagsList = new ArrayList<>();
            Set<String> allowedUserSet = new HashSet<>();
            List<String> allowedGroups = new ArrayList<>();

            // 3. 发起 Feign 调用进行“数据增强”
            try {
//                ResourceInfoGetReqDTO reqDTO = new ResourceInfoGetReqDTO();
//                reqDTO.setResourceId(resourceId);
//                reqDTO.setUserId(SYSTEM_USER_ID);
//                reqDTO.setGroupRoles(Collections.emptyMap());


                R<ResourceItemResponse> responseR = remoteResourceService.getRawResourceInfo(resourceId);

                if (responseR != null && responseR.getCode() != null && responseR.getCode() == ResultCode.SUCCESS.getCode() && responseR.getData() != null) {
                    ResourceItemResponse resourceInfo = responseR.getData();

                    // 覆盖为来自 Resource 服务的权威数据
                    actualResourceType = resourceInfo.getResourceType() != null ? resourceInfo.getResourceType().name() : actualResourceType;
                    resourceName = resourceInfo.getResourceName();

                    if (resourceInfo.getCurrentTags() != null) {
                        tagsList.addAll(resourceInfo.getCurrentTags().values());
                    }
                    if (StrUtil.isNotBlank(resourceInfo.getOwnerId())) {
                        allowedUserSet.add(resourceInfo.getOwnerId());
                    }
                    if (resourceInfo.getSpecifiedUsersGrantedActions() != null) {
                        allowedUserSet.addAll(resourceInfo.getSpecifiedUsersGrantedActions().keySet());
                    }
                } else {
                    log.warn("【数据降级】无法获取 Resource 详情，将使用 DTO 基础数据创建索引. resourceId: {}", resourceId);
                }
            } catch (Exception feignEx) {
                log.error("【网络降级】Resource 服务调用异常，采用基础数据入库: {}", feignEx.getMessage());
            }

            // 4. 构建全量聚合实体
            String esId = SearchIndexEntity.generateEsId(actualResourceType, resourceId);
            SearchIndexEntity searchEntity = SearchIndexEntity.builder()
                    .id(esId)
                    .resourceId(resourceId)
                    .resourceType(actualResourceType)
                    .title(resourceName)
                    .content(rawContent) // 直接使用消息里发来的 content，不再回查
                    .allowedUsers(new ArrayList<>(allowedUserSet))
                    .allowedGroups(allowedGroups)
                    .tags(tagsList)
                    .build();

            // 5. 执行全量 Upsert 落库 ES
            searchSyncService.upsertFullIndex(searchEntity);
            log.info("【搜索服务】成功初始化 ES 索引: {}", esId);

        } catch (Exception e) {
            log.error("【致命错误】消费文档就绪事件失败！", e);
        } finally {
            // 💡 架构关键：无论成功失败，必须清理上下文，防止线程池越权污染！
            SecurityContextHolder.remove();
        }
    }
}