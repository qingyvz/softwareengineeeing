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
            DocumentReadyMessage message = objectMapper.readValue(messageValue, DocumentReadyMessage.class);
            String resourceId = message.getResourceId();
            String rawContent = message.getContent();

            log.info("【搜索服务】接收到文档就绪事件, resourceId: {}, content_length: {}", resourceId, StrUtil.length(rawContent));
            if (StrUtil.isBlank(resourceId)) return;

            // 2. 组装合法的 RPC 请求参数 (完美满足 @NotNull 校验)
            ResourceInfoGetReqDTO reqDTO = new ResourceInfoGetReqDTO();
            reqDTO.setResourceId(resourceId);
            reqDTO.setUserId(SYSTEM_USER_ID);
            reqDTO.setGroupRoles(Collections.emptyMap()); // 必须设置，否则触发 400 校验异常

            // 3. 发起 Feign 调用
            R<ResourceItemResponse> responseR = remoteResourceService.getResourceInfo(reqDTO);

            if (responseR == null || responseR.getCode() == null || responseR.getCode() != ResultCode.SUCCESS.getCode() || responseR.getData() == null) {
                log.warn("无法从 Resource 服务拉取权限详情, 丢弃该消息. resourceId: {}, response: {}", resourceId, responseR);
                return;
            }
            ResourceItemResponse resourceInfo = responseR.getData();

            // 4. 数据结构转换与 ACL 权限聚合
            String actualResourceType = resourceInfo.getResourceType() != null
                    ? resourceInfo.getResourceType().name()
                    : ResourceType.UNKNOWN.name();

            List<String> tagsList = new ArrayList<>();
            if (resourceInfo.getCurrentTags() != null) {
                tagsList.addAll(resourceInfo.getCurrentTags().values());
            }

            Set<String> allowedUserSet = new HashSet<>();
            if (StrUtil.isNotBlank(resourceInfo.getOwnerId())) {
                allowedUserSet.add(resourceInfo.getOwnerId());
            }
            if (resourceInfo.getSpecifiedUsersGrantedActions() != null) {
                allowedUserSet.addAll(resourceInfo.getSpecifiedUsersGrantedActions().keySet());
            }

            List<String> allowedGroups = new ArrayList<>();

            // 5. 构建全量聚合实体
            String esId = SearchIndexEntity.generateEsId(actualResourceType, resourceId);
            SearchIndexEntity searchEntity = SearchIndexEntity.builder()
                    .id(esId)
                    .resourceId(resourceId)
                    .resourceType(actualResourceType)
                    .title(resourceInfo.getResourceName())
                    .content(rawContent)
                    .allowedUsers(new ArrayList<>(allowedUserSet))
                    .allowedGroups(allowedGroups)
                    .tags(tagsList)
                    .build();

            // 6. 执行全量 Upsert 落库 ES
            searchSyncService.upsertFullIndex(searchEntity);

        } catch (Exception e) {
            log.error("【致命错误】消费文档就绪事件失败！", e);
        } finally {
            // 💡 架构关键：无论成功失败，必须清理上下文，防止线程池越权污染！
            SecurityContextHolder.remove();
        }
    }
}