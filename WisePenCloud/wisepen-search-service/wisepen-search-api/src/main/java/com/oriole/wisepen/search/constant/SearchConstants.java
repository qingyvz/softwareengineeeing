package com.oriole.wisepen.search.constant;

/**
 * 搜索服务全局常量
 *
 *
 */
public interface SearchConstants {

    /**
     * 搜索服务名称 (用于 Feign 等服务发现)
     */
    String SERVICE_NAME = "wisepen-search-service";

    /**
     * ES 统一搜索主索引名称
     */
    String INDEX_NAME_MAIN = "wisepen_resource_index";

    /**
     * 默认高亮前缀与后缀
     */
    String HIGHLIGHT_PRE_TAG = "<em class=\"wp-highlight\">";
    String HIGHLIGHT_POST_TAG = "</em>";
}
