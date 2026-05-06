package com.oriole.wisepen.search.controller;

import com.oriole.wisepen.common.core.domain.R;
import com.oriole.wisepen.common.core.domain.enums.BusinessType;
import com.oriole.wisepen.common.log.annotation.Log;
import com.oriole.wisepen.search.domain.dto.req.SearchQueryReqDTO;
import com.oriole.wisepen.search.domain.dto.res.SearchResultResDTO;
import com.oriole.wisepen.search.service.ISearchQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 全局搜索外部接口 (供前端调用)
 */
@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {

    private final ISearchQueryService searchQueryService;

    /**
     * 执行全局聚合搜索
     */
    @PostMapping("/global")
    @Log(
            title = "搜索业务资源",
            businessType = BusinessType.SELECT, // 指向 common.core.domain.enums.BusinessType
            isSaveRequestData = false,  // 关键字可能包含长串无意义字符，防爆库
            isSaveResponseData = false  // 响应结果包含大量高亮标签和列表，严禁存入DB日志
    )
    public R<SearchResultResDTO> globalSearch(@Validated @RequestBody SearchQueryReqDTO reqDTO) {

        // 💡 架构重构：Controller 变薄！
        // 安全上下文的提取、鉴权与 ACL 拼装，全部下沉到 Service 内部闭环。
        // 彻底杜绝了 Controller 层漏传、错传导致越权查询的风险。
        SearchResultResDTO result = searchQueryService.executeGlobalSearch(reqDTO);

        return R.ok(result);
    }
}