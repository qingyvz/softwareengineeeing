package com.oriole.wisepen.resource.constant;

/**
 * 搜索域全局常量。
 * <p>
 * 索引名以 {@code wisepen_resource_index} 落在资源域命名空间下，与 MongoDB 集合
 * {@code wisepen_resource_items} 同源呼应；高亮标签使用统一的 {@code <em class="wp-highlight">}
 * 包裹，前端可对该 class 单独设置样式。
 */
public interface SearchConstants {

    /** ES 资源搜索聚合索引名称 */
    String RESOURCE_INDEX_NAME = "wisepen_resource_index";

    /** 高亮包裹前缀：前端用 {@code v-html} 渲染时通过 class 命中样式 */
    String HIGHLIGHT_PRE_TAG = "<em class=\"wp-highlight\">";

    /** 高亮包裹后缀，与 {@link #HIGHLIGHT_PRE_TAG} 成对出现 */
    String HIGHLIGHT_POST_TAG = "</em>";
}
