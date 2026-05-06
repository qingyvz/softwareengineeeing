package com.oriole.wisepen.search.domain.mq;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 文档解析完成 Kafka 消息 DTO
 */
@Data
public class DocumentParseMsgDTO {
    private Long documentId;
    private String title;
    /** 解析出的全文本正文 */
    private String fullText;
    private LocalDateTime updateTime;
}