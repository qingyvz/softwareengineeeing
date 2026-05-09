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
import com.oriole.wisepen.search.service.ISearchSyncService;

/**
 * 全局搜索外部接口 (供前端调用)
 */
@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {

    private final ISearchQueryService searchQueryService;
    private final ISearchSyncService searchSyncService;

    /**
     * 执行全局聚合搜索
     */
    @PostMapping("/global")
//    @Log(
//            title = "搜索业务资源",
//            businessType = BusinessType.SELECT, // 指向 common.core.domain.enums.BusinessType
//            isSaveRequestData = false,  // 关键字可能包含长串无意义字符，防爆库
//            isSaveResponseData = false  // 响应结果包含大量高亮标签和列表，严禁存入DB日志
//    )
    public R<SearchResultResDTO> globalSearch(@Validated @RequestBody SearchQueryReqDTO reqDTO) {

        // 💡 架构重构：Controller 变薄！
        // 安全上下文的提取、鉴权与 ACL 拼装，全部下沉到 Service 内部闭环。
        // 彻底杜绝了 Controller 层漏传、错传导致越权查询的风险。
        SearchResultResDTO result = searchQueryService.executeGlobalSearch(reqDTO);

        return R.ok(result);
    }

    @PostMapping("/admin/init/all")
    @Log(title = "脚本触发重建全量索引", businessType = BusinessType.OTHER, isSaveRequestData = false)
    public R<String> initAllIndex() {
        // 剥离应用层鉴权，信任网关透传
        java.util.concurrent.CompletableFuture.runAsync(() -> {
//            log.warn("【运维操作】接收到全量 ES 索引重建指令，开始执行...");
            searchSyncService.rebuildDocumentIndex();
            // searchSyncService.rebuildNoteIndex(); // 如果有笔记服务，取消注释
        });

        return R.ok("全量重建任务已接收，正在后台异步拉取库表数据，请观察日志...");
    }
}