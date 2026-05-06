package com.oriole.wisepen.search.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 搜索排序策略枚举
 *
 *
 */
@Getter
@AllArgsConstructor
public enum SearchSortBy {

    RELEVANCE("relevance", "综合相关度排序 (默认)"),
    CREATE_TIME_DESC("create_time_desc", "创建时间降序"),
    UPDATE_TIME_DESC("update_time_desc", "最新更新优先");

    private final String code;
    private final String description;
}
