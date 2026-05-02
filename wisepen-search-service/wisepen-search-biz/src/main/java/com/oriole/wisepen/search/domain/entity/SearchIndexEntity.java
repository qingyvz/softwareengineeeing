package com.oriole.wisepen.search.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Elasticsearch 资源搜索索引实体
 * <p>
 * indexName: 建议在配置文件或常量中统一定义
 * createIndex: 开发环境设为 true，生产环境建议用脚本建索引
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "wisepen_resource_index", createIndex = true)
public class SearchIndexEntity {

    /**
     * ES 文档的唯一 ID，直接使用 resourceId 保持一致
     */
    @Id
    private String id;

    /**
     * 资源业务 ID
     */
    @Field(type = FieldType.Long)
    private Long resourceId;

    /**
     * 资源标题
     * 使用 ik_max_word 建立索引（细粒度），使用 ik_smart 查询（粗粒度）
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String title;

    /**
     * 资源正文内容（笔记的文本，或 PDF 解析出来的文字）
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String content;

    /**
     * 资源类型 (枚举字符串，不分词，用于精确过滤)
     */
    @Field(type = FieldType.Keyword)
    private String resourceType;

    // ================== 权限过滤核心字段 ==================

    /**
     * 允许访问该资源的用户 ID 列表 (精确匹配，不分词)
     * 同步数据时，从 resource-service 的 ComputedGroupAcl 获取
     */
    @Field(type = FieldType.Keyword)
    private List<String> allowedUsers;

    /**
     * 允许访问该资源的小组 ID 列表 (精确匹配，不分词)
     */
    @Field(type = FieldType.Keyword)
    private List<String> allowedGroups;

    // ======================================================

    /**
     * 资源标签 (Keyword数组，用于后续可能的筛选)
     */
    @Field(type = FieldType.Keyword)
    private List<String> tags;

    /**
     * 数据更新时间
     */
    @Field(type = FieldType.Date, format = {}, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
