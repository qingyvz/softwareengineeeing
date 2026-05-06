package com.oriole.wisepen.search.domain.mq;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 笔记文本快照 Kafka 消息 DTO
 */
@Data
public class NoteSnapshotMsgDTO {
    private Long noteId;
    private String title;
    /** 已经由上游剔除掉 HTML/Markdown 标签的纯净文本 */
    private String plainText;
    private LocalDateTime updateTime;
}