package com.oriole.wisepen.resource.service;

import com.oriole.wisepen.resource.domain.entity.SearchIndexEntity;

import java.util.EnumSet;

/**
 * 搜索索引数据同步服务。
 * <p>
 * 所有写操作走 Upsert（"有则局部更新，无则创建"），由
 * {@link com.oriole.wisepen.resource.service.impl.SearchSyncServiceImpl} 实现。
 * 调用方根据语义传入需要更新的字段集合 {@link UpsertField}，避免长正文被零散元数据更新覆盖。
 */
public interface ISearchSyncService {

    /** Upsert 时按需要更新的逻辑字段分组 */
    enum UpsertField {
        /** 资源名称 */
        RESOURCE_NAME,
        /** 正文长文本 */
        CONTENT,
        /** allowedUsers / groupAcls 权限编码 */
        ACL,
        /** tags 列表 */
        TAGS
    }

    /**
     * 通用 Upsert：仅更新 fields 指定字段。resourceId / resourceType / updateTime 始终写入。
     */
    void executeUpsert(SearchIndexEntity entity, EnumSet<UpsertField> fields);

    /** 仅更新元数据与权限（不触碰 content） */
    default void upsertIndexMetaData(SearchIndexEntity entity) {
        executeUpsert(entity, EnumSet.of(UpsertField.ACL, UpsertField.TAGS, UpsertField.RESOURCE_NAME));
    }

    /** 仅更新长文本正文与名称（不触碰 ACL/tags） */
    default void upsertIndexContent(SearchIndexEntity entity) {
        executeUpsert(entity, EnumSet.of(UpsertField.RESOURCE_NAME, UpsertField.CONTENT));
    }

    /** 全量 Upsert：内容 + 权限 + 标签同时落盘，用于初始化索引 */
    default void upsertFullIndex(SearchIndexEntity entity) {
        executeUpsert(entity, EnumSet.allOf(UpsertField.class));
    }

    /** 物理删除 ES 中的索引文档 */
    void deleteIndex(String esId);
}
