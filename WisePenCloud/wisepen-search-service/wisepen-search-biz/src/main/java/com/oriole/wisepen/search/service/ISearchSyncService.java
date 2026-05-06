package com.oriole.wisepen.search.service;



import com.oriole.wisepen.search.domain.entity.SearchIndexEntity;

import java.util.List;

/**
 * 搜索数据同步业务服务接口
 * 【规约遵守】：纯净业务接口，不继承 IService
 */
public interface ISearchSyncService {

    /**
     * 局部更新索引的“元数据与权限” (Upsert)
     * 【核心能力】：仅更新传入的非空字段（如 allowedUsers, tags），绝对不会抹除已存在的长文本 content。
     * 如果该文档尚不存在，则会自动创建。
     *
     * @param entity 包含需更新字段的实体
     */
    void upsertIndexMetaData(SearchIndexEntity entity);

    /**
     * 局部更新索引的“长文本正文” (Upsert)
     * 【核心能力】：专门给 Note/Document 解析服务使用，仅更新 title 和 content。
     *
     * @param entity 包含需更新文本的实体
     */
    void upsertIndexContent(SearchIndexEntity entity);

    /**
     * 批量全量保存或更新 (适用于夜间跑批补偿任务)
     */
    void saveOrUpdateBatch(List<SearchIndexEntity> entities);

    /**
     * 物理删除索引文档
     *
     * @param id ES 中的文档 ID
     */
    void deleteIndex(String id);
}