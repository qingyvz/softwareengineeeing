
package com.oriole.wisepen.search.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.oriole.wisepen.common.core.context.SecurityContextHolder;
import com.oriole.wisepen.common.core.domain.PageResult;
import com.oriole.wisepen.common.core.domain.enums.GroupRoleType;
import com.oriole.wisepen.search.api.domain.dto.req.SearchQueryReqDTO;
import com.oriole.wisepen.search.api.domain.dto.res.SearchHitItemResDTO;
import com.oriole.wisepen.search.domain.entity.SearchIndexEntity;
// 注意：Spring Boot 3.x 使用 co.elastic.clients 构建查询
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
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchQueryServiceImpl {

    // ElasticsearchOperations 是 Spring Data 提供的核心操作模板
    private final ElasticsearchOperations elasticsearchOperations;

    /**
     * 执行全局搜索 (核心引擎)
     */
    public PageResult<SearchHitItemResDTO> executeGlobalSearch(SearchQueryReqDTO req) {
        // 1. 获取当前登录用户上下文 (复用 common 组件)
        Long currentUserId = SecurityContextHolder.getUserId();
        Map<Long, GroupRoleType> groupRoleMap = SecurityContextHolder.getGroupRoleMap();
        List<String> currentGroupIds = new ArrayList<>();
        if (CollUtil.isNotEmpty(groupRoleMap)) {
            currentGroupIds = groupRoleMap.keySet().stream().map(String::valueOf).collect(Collectors.toList());
        }

        log.info("执行搜索, 关键字: {}, 用户ID: {}, 所在小组ID: {}", req.getKeyword(), currentUserId, currentGroupIds);

        // 2. 构建全文检索条件 (匹配标题和内容)
        Query matchQuery = QueryBuilders.multiMatch(m -> m
                .query(req.getKeyword())
                // 标题权重提升3倍，内容保持1倍
                .fields("title^3", "content")
        );

        // 3. 构建权限过滤条件 (Filter 性能极高，不计算得分)
        List<String> finalCurrentGroupIds = currentGroupIds; // Lambda需要final
        Query permissionFilter = QueryBuilders.bool(b -> {
            // 条件A: allowedUsers 包含我
            b.should(QueryBuilders.term(t -> t.field("allowedUsers").value(String.valueOf(currentUserId))));
            
            // 条件B: allowedGroups 包含我所在的任何一个小组
            if (CollUtil.isNotEmpty(finalCurrentGroupIds)) {
                // 将 List<String> 转为 ES 需要的 FieldValue
                List<co.elastic.clients.elasticsearch._types.FieldValue> groupValues = finalCurrentGroupIds.stream()
                        .map(co.elastic.clients.elasticsearch._types.FieldValue::of)
                        .collect(Collectors.toList());
                        
                b.should(QueryBuilders.terms(t -> t
                        .field("allowedGroups")
                        .terms(ts -> ts.value(groupValues))
                ));
            }
            return b;
        });

        // 4. 将检索条件和过滤条件组合为一个大 BoolQuery
        Query finalQuery = QueryBuilders.bool(b -> b
                .must(matchQuery)         // 必须满足关键字匹配
                .filter(permissionFilter) // 结果必须在我的权限范围内
        );

        // 5. 设置高亮 (让命中的关键字变红)
        Highlight highlight = new Highlight(
                List.of(
                        new HighlightField("title"),
                        new HighlightField("content")
                )
        );
        HighlightQuery highlightQuery = new HighlightQuery(highlight, SearchIndexEntity.class);

        // 6. 组装 NativeQuery (包含查询、分页、高亮)
        NativeQuery nativeQuery = new NativeQueryBuilder()
                .withQuery(finalQuery)
                .withPageable(PageRequest.of(req.getPageNum() - 1, req.getPageSize()))
                .withHighlightQuery(highlightQuery)
                .build();

        // 7. 执行查询
        SearchHits<SearchIndexEntity> searchHits = elasticsearchOperations.search(nativeQuery, SearchIndexEntity.class);

        // 8. 封装返回结果
        List<SearchHitItemResDTO> resultList = new ArrayList<>();
        for (SearchHit<SearchIndexEntity> hit : searchHits) {
            SearchIndexEntity entity = hit.getContent();
            SearchHitItemResDTO dto = new SearchHitItemResDTO();
            dto.setResourceId(entity.getResourceId());
            dto.setResourceType(entity.getResourceType());
            dto.setUpdateTime(entity.getUpdateTime());

            // 处理高亮字段替换
            List<String> titleHighlights = hit.getHighlightField("title");
            dto.setTitle(CollUtil.isNotEmpty(titleHighlights) ? titleHighlights.get(0) : entity.getTitle());

            List<String> contentHighlights = hit.getHighlightField("content");
            // 内容通常很长，只截取高亮片段展示作为简介
            if (CollUtil.isNotEmpty(contentHighlights)) {
                dto.setHighlightContent(StrUtil.join("...", contentHighlights));
            } else {
                // 如果内容没高亮(可能只命中了标题)，给一段普通正文简介
                dto.setHighlightContent(StrUtil.sub(entity.getContent(), 0, 100) + "...");
            }
            resultList.add(dto);
        }

  PageResult<SearchHitItemResDTO> pageResult = new PageResult<>(
                searchHits.getTotalHits(),
                req.getPageNum(),
                req.getPageSize()
        );
        pageResult.addAll(resultList);

        return pageResult;
    }
}