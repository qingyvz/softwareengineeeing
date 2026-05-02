
package com.oriole.wisepen.search.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 搜索目标类型枚举
 */
@Getter
@AllArgsConstructor
public enum SearchTargetType {

    /**
     * 全部资源
     */
    ALL("ALL", "全部"),

    /**
     * 仅文档 (PDF, Word 等)
     */
    DOCUMENT("DOCUMENT", "文档"),

    /**
     * 仅笔记
     */
    NOTE("NOTE", "笔记");

    private final String code;
    private final String desc;
    
    // 如果后续需要根据前端传的 string 获取枚举，可以加上这个方法
    public static SearchTargetType getByCode(String code) {
        for (SearchTargetType type : values()) {
            if (type.getCode().equalsIgnoreCase(code)) {
                return type;
            }
        }
        return ALL; // 默认返回全部
    }
}