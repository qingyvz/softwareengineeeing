package com.oriole.wisepen.document.api.domain.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 文档全量文本传输 DTO (供内部微服务拉取正文使用)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentContentDTO implements Serializable {

    /** 业务资源 ID (关联权限体系) */
    private String resourceId;

    /** 文档海量纯净正文 */
    private String content;
}