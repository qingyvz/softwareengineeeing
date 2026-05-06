package com.oriole.wisepen.search.domain.entity;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import com.oriole.wisepen.search.constant.SearchConstants;


import java.time.LocalDateTime;
import java.util.List;

/**
 * Elasticsearch 资源搜索聚合索引实体
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = SearchConstants.INDEX_NAME_MAIN, createIndex = true)
public class SearchIndexEntity {

    /**
     * 【变量作用】: ES 文档的全局唯一 ID。
     * 【生成规则】: resourceType + "_" + resourceId (例如: pdf_10086, note_1001)
     */
    @Id
    private String id;

    /**
     * 【变量作用】: 真实的业务资源 ID。
     * 【来源】: 上游 Resource 服务的 String 类型 ID。
     */
    @Field(type = FieldType.Keyword)
    private String resourceId;

    /**
     * 【变量作用】: 资源类型（如 pdf, docx, note）。
     * 【来源】: 严格对齐上游 ResourceType 枚举。兼顾了前端的类型展示与搜索过滤。
     */
    @Field(type = FieldType.Keyword)
    private String resourceType;

    /**
     * 【变量作用】: 资源标题，使用 ik 分词器建立倒排索引。
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String title;

    /**
     * 【变量作用】: 资源正文内容。
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String content;

    // ================== 权限过滤核心字段 (ACL) ==================

    /**
     * 【变量作用】: 允许访问该资源的用户 ID 列表。
     */
    @Field(type = FieldType.Keyword)
    private List<String> allowedUsers;

    /**
     * 【变量作用】: 允许访问该资源的小组 ID 列表。
     */
    @Field(type = FieldType.Keyword)
    private List<String> allowedGroups;

    // ======================================================

    /**
     * 【变量作用】: 资源标签。
     */
    @Field(type = FieldType.Keyword)
    private List<String> tags;

    /**
     * 【变量作用】: 业务最新更新时间。
     * 【架构优化】: 采用多 pattern 兼容模式，同时支持 ISO 标准格式、普通空格格式以及毫秒时间戳
     */
    @Field(type = FieldType.Date, format = {}, pattern = "uuuu-MM-dd'T'HH:mm:ss||uuuu-MM-dd HH:mm:ss||epoch_millis")
    private LocalDateTime updateTime;
    /**
     * 生成防冲突的 ES 全局唯一 ID
     */
    public static String generateEsId(String resourceType, String resourceId) {
        if (resourceType == null || resourceId == null) {
            throw new IllegalArgumentException("生成 ES ID 时，resourceType 和 resourceId 不能为空");
        }
        return resourceType.toLowerCase() + "_" + resourceId;
    }
}