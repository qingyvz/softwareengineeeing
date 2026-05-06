package com.oriole.wisepen.search.enums;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 搜索目标业务类型枚举
 *
 *
 */
@Getter
@AllArgsConstructor
public enum SearchTargetType {

    NOTE("note", "学习笔记"),
    DOCUMENT("document", "文档资料");

    private final String code;
    private final String description;
}