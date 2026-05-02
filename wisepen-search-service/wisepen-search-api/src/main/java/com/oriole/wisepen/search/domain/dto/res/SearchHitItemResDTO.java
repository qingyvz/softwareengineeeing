package com.oriole.wisepen.search.api.domain.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 搜索命中的结果项 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchHitItemResDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 资源ID (前端拿着这个ID去调用详情接口)
     */
    private Long resourceId;

    /**
     * 资源标题 (可能包含高亮标签 <em>...</em>)
     */
    private String title;

    /**
     * 资源高亮内容片段 (用于在搜索列表中展示简介)
     */
    private String highlightContent;

    /**
     * 资源类型 (Note, Document等，前端用于展示不同图标)
     */
    private String resourceType;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
