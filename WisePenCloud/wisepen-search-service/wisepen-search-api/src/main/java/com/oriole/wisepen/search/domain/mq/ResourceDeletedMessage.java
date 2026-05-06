package com.oriole.wisepen.search.domain.mq;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 接收上游：资源硬删除广播事件
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResourceDeletedMessage implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 按资源类型分组的待删除资源ID列表
     * 注：上游是 Map<ResourceType, List<String>>，经过 @JsonValue 序列化后，
     * Key 会变成 "pdf", "note" 等字符串，所以我们用 Map<String, List<String>> 安全接收。
     */
    private Map<String, List<String>> typedResourceIds;
}