package com.oriole.wisepen.search.consumer;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oriole.wisepen.common.core.context.SecurityContextHolder;
import com.oriole.wisepen.common.core.domain.R;
import com.oriole.wisepen.common.core.domain.enums.IdentityType;
import com.oriole.wisepen.common.core.domain.enums.ResultCode; // 引入 ResultCode
import com.oriole.wisepen.common.core.exception.ServiceException;
import com.oriole.wisepen.note.api.constant.MqTopicConstants;
import com.oriole.wisepen.note.api.domain.dto.res.NoteSnapshotResponse; // 注意：路径以你实际的为准
import com.oriole.wisepen.note.api.domain.mq.NoteSnapshotMessage;
import com.oriole.wisepen.note.api.feign.RemoteNoteService;
import com.oriole.wisepen.search.domain.entity.SearchIndexEntity;
import com.oriole.wisepen.search.service.ISearchSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 笔记模块快照消费者 (终极防弹版)
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class NoteSnapshotConsumer {

    private final ISearchSyncService searchSyncService;
    private final RemoteNoteService remoteNoteService;
    private final ObjectMapper objectMapper;
    private final ScheduledExecutorService scheduler;

    private static final String RESOURCE_TYPE_NOTE = "note";
    private static final Long SYSTEM_USER_ID = 0L;

    // ⚡ 核心合并点：挂上隔离工厂，换上新马甲 ⚡
    @KafkaListener(
            topics = MqTopicConstants.TOPIC_NOTE_SNAPSHOT,
            groupId = "wisepen-search-note-reborn", // 1. 换个新名字跳过毒药消息
            containerFactory = "rawStringContainerFactory" // 2. 强行使用纯字符串工厂，免疫所有序列化报错
    )
    public void onNoteSnapshot(ConsumerRecord<String, String> record) {
        // 1. 【加固：毒消息拦截】
        NoteSnapshotMessage msg;
        try {
            // 3. 原本的 JSON 转换逻辑保持不变，因为传入的 record.value() 已经是纯 String 了
            msg = objectMapper.readValue(record.value(), NoteSnapshotMessage.class);
        } catch (JsonProcessingException e) {
            log.error("笔记快照消息格式致命错误，丢弃。offset: {}, payload: {}", record.offset(), record.value());
            return;
        }

        if (msg == null || StrUtil.isBlank(msg.getResourceId())) {
            return;
        }

        // 2. 【分段防御】
        log.info("收到笔记正文更新消息 [{}], 启动异步延时补偿任务", msg.getResourceId());

        String resourceId = msg.getResourceId();
        String plainText = msg.getPlainText();

        scheduler.schedule(() -> safeSync(resourceId, plainText), 3, TimeUnit.SECONDS);
    }

    private void safeSync(String resourceId, String plainText) {
        try {
            // 3. 【加固：上下文自愈】
            SecurityContextHolder.setUserId(SYSTEM_USER_ID);
            SecurityContextHolder.setIdentityType(IdentityType.ADMIN.getCode());

            // 4. 【加固：RPC 鲁棒性】
            R<NoteSnapshotResponse> response;
            try {
                response = remoteNoteService.getNoteLatestVersion(resourceId);
            } catch (Exception e) {
                log.warn("笔记服务 RPC 通讯异常，资源同步任务挂起。id: {}, error: {}", resourceId, e.getMessage());
                return;
            }

            // 5. ⚡ 核心合并点：完善业务校验逻辑 ⚡
            // 必须校验 code == SUCCESS，防止下游报错返回 500 时，data 为 null 引发空指针
            if (response == null || response.getCode() == null || response.getCode() != ResultCode.SUCCESS.getCode() || response.getData() == null) {
                log.warn("笔记快照状态校验未通过（可能资源不存在或拉取失败），放弃 ES 同步。id: {}", resourceId);
                return;
            }

            // 6. 【执行同步】
            SearchIndexEntity entity = SearchIndexEntity.builder()
                    .id(SearchIndexEntity.generateEsId(RESOURCE_TYPE_NOTE, resourceId))
                    .resourceId(resourceId)
                    .resourceType(RESOURCE_TYPE_NOTE)
                    .content(plainText)
                    .build();

            searchSyncService.upsertIndexContent(entity);
            log.info("笔记正文同步完成。esId: {}", entity.getId());

        } catch (ServiceException se) {
            log.error("笔记同步业务异常: {}, id: {}", se.getMessage(), resourceId);
        } catch (Exception e) {
            log.error("笔记同步系统级严重异常: ", e);
        } finally {
            // 7. 【终极纪律：清理上下文】
            SecurityContextHolder.remove();
        }
    }
}