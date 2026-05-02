package com.oriole.wisepen.search.repository;

import com.oriole.wisepen.search.domain.entity.SearchIndexEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * ES 搜索索引仓储接口
 * 泛型1: 实体类类型 (SearchIndexEntity)
 * 泛型2: 主键类型 (String)
 */
@Repository
public interface SearchIndexRepository extends ElasticsearchRepository<SearchIndexEntity, String> {
    
    // 这里空着就行！Spring Data Elasticsearch 会自动实现基础的保存、删除等方法。
    // 复杂的全文检索我们已经在 SearchQueryServiceImpl 里用 ElasticsearchOperations 实现了。

}
