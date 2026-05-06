package com.oriole.wisepen.search.domain.base;

import com.oriole.wisepen.search.enums.SearchTargetType;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * 搜索命中结果基础 DTO
 *
 * @author Architect
 */
@Data
public class SearchHitBase implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 业务主键 ID (对应 Document/Note/Resource 的 ID)
     */
    private String bizId;

    /**
     * 数据归属类型
     */
    private SearchTargetType targetType;

    /**
     * 标题 (若命中则包含高亮标签)
     */
    private String title;

    /**
     * 摘要/内容截取 (若命中则包含高亮标签)
     */
    private String summary;

    /**
     * 原文作者/创建人
     */
    private String author;

    /**
     * 业务创建时间
     */
    private LocalDateTime bizCreateTime;

    /**
     * 扩展字段 (针对不同业务线的特定数据透传)
     */
    private Map<String, Object> extData;
}
