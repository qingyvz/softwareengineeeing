package com.oriole.wisepen.search.service;

import com.oriole.wisepen.search.domain.dto.req.SearchQueryReqDTO;
import com.oriole.wisepen.search.domain.dto.res.SearchResultResDTO;

/**
 * 搜索查询业务服务接口
 */
public interface ISearchQueryService {

    /**
     * 全局高聚合搜索 (由上下文自动处理权限过滤)
     */
    SearchResultResDTO executeGlobalSearch(SearchQueryReqDTO req);

    /**
     * 内部全局搜索 (绕过权限，供系统内部调用)
     */
    SearchResultResDTO executeInternalSearch(SearchQueryReqDTO req);
}