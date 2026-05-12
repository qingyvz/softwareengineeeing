package com.oriole.wisepen.resource.service.impl;

import cn.hutool.core.util.StrUtil;
import com.oriole.wisepen.resource.domain.entity.SearchIndexEntity;
import com.oriole.wisepen.resource.repository.SearchIndexRepository;
import com.oriole.wisepen.resource.service.ISearchSyncService;
import com.oriole.wisepen.resource.enums.UpsertField;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.EnumSet;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchSyncServiceImpl implements ISearchSyncService {

    private final ElasticsearchOperations elasticsearchOperations;
    private final SearchIndexRepository searchIndexRepository;

    @Override
    public void executeUpsert(SearchIndexEntity entity, EnumSet<UpsertField> fields) {
        if (entity == null || StrUtil.isBlank(entity.getId())) {
            log.warn("searchIndex upsert skipped reason=missing-id");
            return;
        }
        Document document = Document.create();

        // resourceId / resourceType / updateTime 始终落盘，否则 Upsert 出来的新文档会缺关键字段
        if (StrUtil.isNotBlank(entity.getResourceId())) {
            document.put("resourceId", entity.getResourceId());
        }
        if (StrUtil.isNotBlank(entity.getResourceType())) {
            document.put("resourceType", entity.getResourceType());
        }
        document.put("updateTime", LocalDateTime.now());

        if (fields.contains(UpsertField.RESOURCE_NAME) && StrUtil.isNotBlank(entity.getResourceName())) {
            document.put("resourceName", entity.getResourceName());
        }
        if (fields.contains(UpsertField.CONTENT) && StrUtil.isNotBlank(entity.getContent())) {
            document.put("content", entity.getContent());
        }
        if (fields.contains(UpsertField.ACL)) {
            // allowedUsers / groupAcls 允许空集合（语义=清空白名单），但必须落到 ES 而非保留旧值
            if (entity.getAllowedUsers() != null) {
                document.put("allowedUsers", entity.getAllowedUsers());
            }
            if (entity.getGroupAcls() != null) {
                document.put("groupAcls", entity.getGroupAcls());
            }
        }
        if (fields.contains(UpsertField.TAGS) && entity.getTags() != null) {
            document.put("tags", entity.getTags());
        }

        UpdateQuery updateQuery = UpdateQuery.builder(entity.getId())
                .withDocument(document)
                .withDocAsUpsert(true)
                .build();
        IndexCoordinates indexCoordinates = elasticsearchOperations.getIndexCoordinatesFor(SearchIndexEntity.class);
        elasticsearchOperations.update(updateQuery, indexCoordinates);

        log.debug("searchIndex upsert done esId={} fields={}", entity.getId(), fields);
    }

    @Override
    public void deleteIndex(String esId) {
        if (StrUtil.isBlank(esId)) {
            return;
        }
        searchIndexRepository.deleteById(esId);
        log.info("searchIndex deleted esId={}", esId);
    }
}
