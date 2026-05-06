package com.oriole.wisepen.search.service.impl;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.oriole.wisepen.search.domain.entity.SearchIndexEntity;
import com.oriole.wisepen.search.repository.SearchIndexRepository;
import com.oriole.wisepen.search.service.ISearchSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 搜索数据同步业务服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SearchSyncServiceImpl implements ISearchSyncService {

    // 👉 注入大杀器：处理高阶操作（Upsert 局部更新）
    private final ElasticsearchOperations elasticsearchOperations;

    // 👉 注入 Repository：处理基础的 CRUD（批量保存、根据 ID 删除）
    private final SearchIndexRepository searchIndexRepository;

    @Override
    public void upsertIndexMetaData(SearchIndexEntity entity) {
        Document document = Document.create();

        if (StrUtil.isNotBlank(entity.getResourceId())) {
            document.put("resourceId", entity.getResourceId());
        }
        if (StrUtil.isNotBlank(entity.getResourceType())) {
            document.put("resourceType", entity.getResourceType());
        }
        // 允许设为空列表(清空权限)，但不能为 null
        if (entity.getAllowedUsers() != null) {
            document.put("allowedUsers", entity.getAllowedUsers());
        }
        if (entity.getAllowedGroups() != null) {
            document.put("allowedGroups", entity.getAllowedGroups());
        }
        if (entity.getTags() != null) {
            document.put("tags", entity.getTags());
        }

        document.put("updateTime", LocalDateTime.now());

        executeUpsert(entity.getId(), document);
        log.debug("成功将资源的 [元数据与权限] Upsert 到 ES, esId: {}", entity.getId());
    }

    @Override
    public void upsertIndexContent(SearchIndexEntity entity) {
        Document document = Document.create();

        if (StrUtil.isNotBlank(entity.getResourceId())) {
            document.put("resourceId", entity.getResourceId());
        }
        if (StrUtil.isNotBlank(entity.getResourceType())) {
            document.put("resourceType", entity.getResourceType());
        }
        if (StrUtil.isNotBlank(entity.getTitle())) {
            document.put("title", entity.getTitle());
        }
        if (StrUtil.isNotBlank(entity.getContent())) {
            document.put("content", entity.getContent());
        }

        document.put("updateTime", LocalDateTime.now());

        executeUpsert(entity.getId(), document);
        log.debug("成功将资源的 [长文本正文] Upsert 到 ES, esId: {}", entity.getId());
    }

    @Override
    public void saveOrUpdateBatch(List<SearchIndexEntity> entities) {
        if (CollUtil.isEmpty(entities)) {
            return;
        }
        // 👉 使用 Repository 极其清爽的批量保存
        searchIndexRepository.saveAll(entities);
        log.info("成功批量同步资源索引到 ES, 数量: {}", entities.size());
    }

    @Override
    public void deleteIndex(String id) {
        if (StrUtil.isBlank(id)) return;
        // 👉 使用 Repository 根据 ID 直接物理删除
        searchIndexRepository.deleteById(id);
        log.info("成功从 ES 中硬删除索引, esId: {}", id);
    }

    /**
     * 抽取公共的 Upsert 底层执行逻辑
     */
    private void executeUpsert(String id, Document document) {
        // 构造 UpdateQuery，withDocAsUpsert(true) 代表“有则局部更新，无则创建”
        UpdateQuery updateQuery = UpdateQuery.builder(id)
                .withDocument(document)
                .withDocAsUpsert(true)
                .build();

        // 动态获取 @Document 注解上声明的索引名称
        IndexCoordinates indexCoordinates = elasticsearchOperations.getIndexCoordinatesFor(SearchIndexEntity.class);
        elasticsearchOperations.update(updateQuery, indexCoordinates);
    }
}