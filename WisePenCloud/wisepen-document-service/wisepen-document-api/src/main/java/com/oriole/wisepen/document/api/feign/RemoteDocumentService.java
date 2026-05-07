package com.oriole.wisepen.document.api.feign;

import com.oriole.wisepen.common.core.domain.PageResult;
import com.oriole.wisepen.common.core.domain.R;
import com.oriole.wisepen.document.api.domain.dto.DocumentContentDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(contextId = "remoteDocumentService", value = "wisepen-document-service")
public interface RemoteDocumentService {

    /**
     * 分页爬取全量文档正文
     */
    @GetMapping("/internal/document/content/page")
    R<PageResult<DocumentContentDTO>> getDocumentContentPage(
            @RequestParam("page") int page,
            @RequestParam("size") int size
    );
}