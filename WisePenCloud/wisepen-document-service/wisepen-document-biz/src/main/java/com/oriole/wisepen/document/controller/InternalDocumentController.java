package com.oriole.wisepen.document.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.oriole.wisepen.common.core.domain.PageResult;
import com.oriole.wisepen.common.core.domain.R;
import com.oriole.wisepen.document.api.domain.dto.DocumentContentDTO;
import com.oriole.wisepen.document.domain.entity.DocumentContentEntity;
import com.oriole.wisepen.document.domain.entity.DocumentInfoEntity;
import com.oriole.wisepen.document.repository.DocumentContentRepository;
import com.oriole.wisepen.document.repository.DocumentInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/internal/document")
@RequiredArgsConstructor
public class InternalDocumentController {


    private final DocumentContentRepository documentContentRepository;
    private final DocumentInfoRepository documentInfoRepository;

    /**
     * 内部接口：供 Search 服务全量爬取数据用于重建 ES 索引
     */
    @GetMapping("/content/page")
    public R<PageResult<DocumentContentDTO>> getDocumentContentPage(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "100") int size) {

        // 1. 从 Mongo 分页拉取内容表 (注意 Spring Data Page 从 0 开始)
        Page<DocumentContentEntity> contentPage = documentContentRepository.findAll(PageRequest.of(page - 1, size));

        if (contentPage.isEmpty()) {
            return R.ok(new PageResult<>(contentPage.getTotalElements(), page, size));
        }

        // 2. 提取出这批数据的 documentId 集合
        List<String> documentIds = contentPage.getContent().stream()
                .map(DocumentContentEntity::getDocumentId)
                .collect(Collectors.toList());

        // 3. 去 Info 表查出对应的 resourceId 进行组装
        List<DocumentInfoEntity> infoList = (List<DocumentInfoEntity>) documentInfoRepository.findAllById(documentIds);
        Map<String, String> docToResourceMap = infoList.stream()
                .filter(info -> info.getResourceId() != null)
                .collect(Collectors.toMap(DocumentInfoEntity::getDocumentId, DocumentInfoEntity::getResourceId));

        // 4. 组装返回 DTO
        List<DocumentContentDTO> exportList = contentPage.getContent().stream()
                .filter(c -> docToResourceMap.containsKey(c.getDocumentId()))
                .map(c -> new DocumentContentDTO(docToResourceMap.get(c.getDocumentId()), c.getRawText()))
                .collect(Collectors.toList());

        PageResult<DocumentContentDTO> result = new PageResult<>(contentPage.getTotalElements(), page, size);
        result.setList(exportList);

        return R.ok(result);
    }
}