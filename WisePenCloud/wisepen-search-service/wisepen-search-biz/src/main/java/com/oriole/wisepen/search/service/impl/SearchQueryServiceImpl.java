package com.oriole.wisepen.search.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.oriole.wisepen.common.core.context.SecurityContextHolder;
import com.oriole.wisepen.search.constant.SearchConstants;
import com.oriole.wisepen.search.domain.dto.req.SearchQueryReqDTO;
import com.oriole.wisepen.search.domain.dto.res.SearchHitItemResDTO;
import com.oriole.wisepen.search.domain.dto.res.SearchResultResDTO;
import com.oriole.wisepen.search.domain.entity.SearchIndexEntity;
import com.oriole.wisepen.search.service.ISearchQueryService;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.HighlightQuery;
import org.springframework.data.elasticsearch.core.query.highlight.Highlight;
import org.springframework.data.elasticsearch.core.query.highlight.HighlightField;
import org.springframework.data.elasticsearch.core.query.highlight.HighlightParameters;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 搜索查询业务服务实现 (重构加固版)
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SearchQueryServiceImpl implements ISearchQueryService {

    private final ElasticsearchOperations elasticsearchOperations;

    @Override
    public SearchResultResDTO executeGlobalSearch(SearchQueryReqDTO req) {
        // 1. 从安全上下文自愈身份
        Long userId = SecurityContextHolder.getUserId();
        Map<Long, ?> groupMap = SecurityContextHolder.getGroupRoleMap();
        List<String> groupIds = groupMap.keySet().stream().map(String::valueOf).collect(Collectors.toList());

        return search(req, userId, groupIds, true);
    }

    @Override
    public SearchResultResDTO executeInternalSearch(SearchQueryReqDTO req) {
        // 内部搜索，userId 传 null 绕过 ACL
        return search(req, null, null, false);
    }

    /**
     * 核心搜索驱动方法
     */
    private SearchResultResDTO search(SearchQueryReqDTO req, Long userId, List<String> groupIds, boolean enableAcl) {
        long startTime = System.currentTimeMillis();

        // 1. 构建布尔查询容器
        BoolQuery.Builder boolBuilder = QueryBuilders.bool();

        // 2. 全文检索 (评分项)
        boolBuilder.must(QueryBuilders.multiMatch(m -> m
                .query(req.getKeyword())
                .fields("title^3", "content") // 标题权重提升
        ));

        // 3. 权限隔离过滤器 (非评分项，支持缓存)
        if (enableAcl && userId != null) {
            boolBuilder.filter(f -> f.bool(b -> {
                // 满足用户白名单 OR 满足组白名单
                b.should(s -> s.term(t -> t.field("allowedUsers").value(String.valueOf(userId))));
                if (CollUtil.isNotEmpty(groupIds)) {
                    List<FieldValue> groupValues = groupIds.stream().map(FieldValue::of).collect(Collectors.toList());
                    b.should(s -> s.terms(t -> t.field("allowedGroups").terms(ts -> ts.value(groupValues))));
                }
                return b;
            }));
        }

        // 4. 类型过滤器
        if (req.getTargetType() != null) {
            boolBuilder.filter(f -> f.term(t -> t.field("resourceType").value(req.getTargetType().name().toLowerCase())));
        }

        // 5. 高亮与分页构建
        NativeQuery nativeQuery = buildNativeQuery(boolBuilder.build()._toQuery(), req);

        // 6. 执行与解析
        SearchHits<SearchIndexEntity> searchHits = elasticsearchOperations.search(nativeQuery, SearchIndexEntity.class);
        List<SearchHitItemResDTO> items = searchHits.stream()
                .map(this::mapToResDTO)
                .collect(Collectors.toList());

        return SearchResultResDTO.builder()
                .total(searchHits.getTotalHits())
                .tookInMillis(System.currentTimeMillis() - startTime)
                .items(items)
                .build();
    }

    private NativeQuery buildNativeQuery(Query query, SearchQueryReqDTO req) {
        HighlightParameters hlParams = HighlightParameters.builder()
                .withPreTags(SearchConstants.HIGHLIGHT_PRE_TAG)
                .withPostTags(SearchConstants.HIGHLIGHT_POST_TAG)
                .withFragmentSize(100) // 每个片段长度
                .withNumberOfFragments(3) // 返回片段数量
                .build();

        Highlight highlight = new Highlight(hlParams, List.of(
                new HighlightField("title"),
                new HighlightField("content")
        ));

        return new NativeQueryBuilder()
                .withQuery(query)
                .withPageable(PageRequest.of(req.getPageNum() - 1, req.getPageSize()))
                .withHighlightQuery(new HighlightQuery(highlight, SearchIndexEntity.class))
                .build();
    }

    private SearchHitItemResDTO mapToResDTO(SearchHit<SearchIndexEntity> hit) {
        SearchIndexEntity entity = hit.getContent();

        // 提取标题高亮
        String highTitle = hit.getHighlightField("title").stream().findFirst().orElse(entity.getTitle());

        // 提取并拼接正文高亮，若无则取前100字截断
        List<String> contentHighlights = hit.getHighlightField("content");
        String highContent = CollUtil.isNotEmpty(contentHighlights) ?
                StrUtil.join("...", contentHighlights) :
                StrUtil.sub(entity.getContent(), 0, 100) + "...";

        return SearchHitItemResDTO.builder()
                .resourceId(entity.getResourceId())
                .resourceType(entity.getResourceType())
                .title(highTitle)
                .highlightContent(highContent)
                .updateTime(entity.getUpdateTime())
                .build();
    }
}