package com.oriole.wisepen.search.controller;

import com.oriole.wisepen.common.core.domain.R;
import com.oriole.wisepen.search.domain.dto.req.SearchQueryReqDTO;
import com.oriole.wisepen.search.domain.dto.res.SearchResultResDTO;
import com.oriole.wisepen.search.service.ISearchQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 内部搜索服务接口 (供其他微服务通过 FeignClient RPC 调用)
 */
@Slf4j
@RestController
@RequestMapping("/internal/search")
@RequiredArgsConstructor
public class InternalSearchController {

    private final ISearchQueryService searchQueryService;

    /**
     * 内部聚合通用搜索 (无用户上下文，全量/超级权限搜索)
     */
    @PostMapping("/query")
    // 💡 内部接口不加 @Log 注解，避免产生多余的内部 RPC 审计日志
    public R<SearchResultResDTO> search(@RequestBody SearchQueryReqDTO reqDTO) {

        log.info("内部微服务触发全量搜索调用，关键字: {}", reqDTO.getKeyword());

        // 💡 架构重构：明确语义！
        // 直接调用专属的 internal 方法，底层自动绕过 allowedUsers 和 allowedGroups 过滤。
        SearchResultResDTO result = searchQueryService.executeInternalSearch(reqDTO);

        return R.ok(result);
    }
}