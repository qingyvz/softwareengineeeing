
package com.oriole.wisepen.search.controller;

import com.oriole.wisepen.common.core.domain.PageResult;
import com.oriole.wisepen.common.core.domain.R;
import com.oriole.wisepen.common.security.annotation.CheckLogin;
import com.oriole.wisepen.search.api.domain.dto.req.SearchQueryReqDTO;
import com.oriole.wisepen.search.api.domain.dto.res.SearchHitItemResDTO;
import com.oriole.wisepen.search.service.impl.SearchQueryServiceImpl;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;

/**
 * 全文搜索控制器 (面向前端C端)
 */
@RestController
@RequestMapping("/search")
public class SearchController {

    @Resource
    private SearchQueryServiceImpl searchQueryService;

    /**
     * 全局全文检索接口
     *
     * @param reqDTO 包含关键字、分页等参数
     * @return 带有高亮的资源分页列表
     */
    // @CheckLogin // 复用你们的鉴权注解，确保只有登录用户能调用
    @PostMapping("/query")
    public R<PageResult<SearchHitItemResDTO>> query(@Validated @RequestBody SearchQueryReqDTO reqDTO) {
        
        // 调用我们写好的核心查询逻辑
          com.oriole.wisepen.common.core.context.SecurityContextHolder.setUserId(1001L);
        // ===============================================================

        PageResult<SearchHitItemResDTO> result = searchQueryService.executeGlobalSearch(reqDTO);
        
        return R.ok(result);
        // PageResult<SearchHitItemResDTO> result = searchQueryService.executeGlobalSearch(reqDTO);
        
        // return R.ok(result);
    }

    // 加上这个最简单的 GET 请求测试接口
    @GetMapping("/ping")
    public String ping() {
        return "pong! 搜索服务已连通!";
    }
}