package com.oriole.wisepen.search.service.impl;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.oriole.wisepen.common.core.context.SecurityContextHolder;
import com.oriole.wisepen.common.core.domain.PageResult;
import com.oriole.wisepen.common.core.domain.R;
import com.oriole.wisepen.common.core.domain.enums.IdentityType;
import com.oriole.wisepen.common.core.domain.enums.ResultCode;
import com.oriole.wisepen.document.api.domain.dto.DocumentContentDTO;
import com.oriole.wisepen.document.api.feign.RemoteDocumentService;
import com.oriole.wisepen.resource.domain.dto.ResourceInfoGetReqDTO;
import com.oriole.wisepen.resource.domain.dto.res.ResourceItemResponse;
import com.oriole.wisepen.resource.enums.ResourceType;
import com.oriole.wisepen.resource.feign.RemoteResourceService;
import com.oriole.wisepen.search.domain.entity.SearchIndexEntity;
import com.oriole.wisepen.search.repository.SearchIndexRepository;
import com.oriole.wisepen.search.service.ISearchSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

/**
 * 搜索数据同步业务服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SearchSyncServiceImpl implements ISearchSyncService {

    // 👉 注入大杀器：处理高阶操作（Upsert 局部更新）
    private final ElasticsearchOperations elasticsearchOperations;

    // 👉 注入 Repository：处理基础的 CRUD（批量保存、根据 ID 删除）
    private final SearchIndexRepository searchIndexRepository;


    @Autowired
    private RemoteDocumentService remoteDocumentService;
    @Autowired
    private RemoteResourceService remoteResourceService;
    // @Autowired
    // private RemoteNoteService remoteNoteService; // 如果你们有笔记的 Feign，请按需注入

    // 系统管理员虚拟 ID
    private static final Long SYSTEM_USER_ID = 0L;
    @Override
    public void upsertIndexMetaData(SearchIndexEntity entity) {
        Document document = Document.create();

        if (StrUtil.isNotBlank(entity.getResourceId())) {
            document.put("resourceId", entity.getResourceId());
        }
        if (StrUtil.isNotBlank(entity.getResourceType())) {
            document.put("resourceType", entity.getResourceType());
        }
        // 允许设为空列表(清空权限)，但不能为 null
        if (entity.getAllowedUsers() != null) {
            document.put("allowedUsers", entity.getAllowedUsers());
        }
        if (entity.getAllowedGroups() != null) {
            document.put("allowedGroups", entity.getAllowedGroups());
        }
        if (entity.getTags() != null) {
            document.put("tags", entity.getTags());
        }

        document.put("updateTime", LocalDateTime.now());

        executeUpsert(entity.getId(), document);
        log.debug("成功将资源的 [元数据与权限] Upsert 到 ES, esId: {}", entity.getId());
    }

    @Override
    public void upsertIndexContent(SearchIndexEntity entity) {
        Document document = Document.create();

        if (StrUtil.isNotBlank(entity.getResourceId())) {
            document.put("resourceId", entity.getResourceId());
        }
        if (StrUtil.isNotBlank(entity.getResourceType())) {
            document.put("resourceType", entity.getResourceType());
        }
        if (StrUtil.isNotBlank(entity.getTitle())) {
            document.put("title", entity.getTitle());
        }
        if (StrUtil.isNotBlank(entity.getContent())) {
            document.put("content", entity.getContent());
        }

        document.put("updateTime", LocalDateTime.now());

        executeUpsert(entity.getId(), document);
        log.debug("成功将资源的 [长文本正文] Upsert 到 ES, esId: {}", entity.getId());
    }
    @Override
    public void upsertFullIndex(SearchIndexEntity entity) {
        Document document = Document.create();

        // ================= 1. 组装元数据与权限 =================
        if (StrUtil.isNotBlank(entity.getResourceId())) {
            document.put("resourceId", entity.getResourceId());
        }
        if (StrUtil.isNotBlank(entity.getResourceType())) {
            document.put("resourceType", entity.getResourceType());
        }
        // 允许设为空列表(清空权限)，但不能为 null
        if (entity.getAllowedUsers() != null) {
            document.put("allowedUsers", entity.getAllowedUsers());
        }
        if (entity.getAllowedGroups() != null) {
            document.put("allowedGroups", entity.getAllowedGroups());
        }
        if (entity.getTags() != null) {
            document.put("tags", entity.getTags());
        }

        // ================= 2. 组装内容与标题 =================
        if (StrUtil.isNotBlank(entity.getTitle())) {
            document.put("title", entity.getTitle());
        }
        if (StrUtil.isNotBlank(entity.getContent())) {
            document.put("content", entity.getContent());
        }

        // 刷新更新时间
        document.put("updateTime", LocalDateTime.now());

        // 执行底层复用的 Upsert 操作
        executeUpsert(entity.getId(), document);
        log.info("成功将资源的 [全量数据(内容+权限)] Upsert 到 ES, esId: {}", entity.getId());
    }

    @Override
    public void saveOrUpdateBatch(List<SearchIndexEntity> entities) {
        if (CollUtil.isEmpty(entities)) {
            return;
        }
        // 👉 使用 Repository 极其清爽的批量保存
        searchIndexRepository.saveAll(entities);
        log.info("成功批量同步资源索引到 ES, 数量: {}", entities.size());
    }

    @Override
    public void deleteIndex(String id) {
        if (StrUtil.isBlank(id)) return;
        // 👉 使用 Repository 根据 ID 直接物理删除
        searchIndexRepository.deleteById(id);
        log.info("成功从 ES 中硬删除索引, esId: {}", id);
    }

    /**
     * 抽取公共的 Upsert 底层执行逻辑
     */
    private void executeUpsert(String id, Document document) {
        // 构造 UpdateQuery，withDocAsUpsert(true) 代表“有则局部更新，无则创建”
        UpdateQuery updateQuery = UpdateQuery.builder(id)
                .withDocument(document)
                .withDocAsUpsert(true)
                .build();

        // 动态获取 @Document 注解上声明的索引名称
        IndexCoordinates indexCoordinates = elasticsearchOperations.getIndexCoordinatesFor(SearchIndexEntity.class);
        elasticsearchOperations.update(updateQuery, indexCoordinates);
    }


    @Override
    public void rebuildDocumentIndex() {
        log.info("============== 开始执行【文档】全量索引补偿爬虫任务 ==============");
        long start = System.currentTimeMillis();
        int page = 1;
        int size = 50;
        int totalProcessed = 0;

        try {
            // 💡 架构关键：在异步线程模拟系统管理员身份
            SecurityContextHolder.setUserId(SYSTEM_USER_ID);
            SecurityContextHolder.setIdentityType(IdentityType.ADMIN.getCode());

            while (true) {
                // 1. 去 Document 服务拉正文
                R<PageResult<DocumentContentDTO>> pageR = remoteDocumentService.getDocumentContentPage(page, size);

                if (pageR == null || pageR.getCode() == null || pageR.getCode() != ResultCode.SUCCESS.getCode() || pageR.getData() == null) {
                    log.error("拉取 Document 文本失败, 中断跑批!");
                    break;
                }

                List<DocumentContentDTO> documentList = pageR.getData().getList();
                if (documentList.isEmpty()) {
                    break;
                }

                List<SearchIndexEntity> batchEntities = new ArrayList<>();

                // 2. 遍历文本，去 Resource 服务拉取 ACL 权限与元数据
                for (DocumentContentDTO doc : documentList) {
                    try {
                        ResourceInfoGetReqDTO reqDTO = new ResourceInfoGetReqDTO();
                        reqDTO.setResourceId(doc.getResourceId());
                        reqDTO.setUserId(SYSTEM_USER_ID);
                        reqDTO.setGroupRoles(Collections.emptyMap());

                        R<ResourceItemResponse> resR = remoteResourceService.getResourceInfo(reqDTO);

                        if (resR != null && resR.getCode() != null && resR.getCode() == ResultCode.SUCCESS.getCode() && resR.getData() != null) {
                            ResourceItemResponse resInfo = resR.getData();

                            // 类型
                            String actualResourceType = resInfo.getResourceType() != null
                                    ? resInfo.getResourceType().name() : ResourceType.UNKNOWN.name();

                            // 标签
                            List<String> tagsList = new ArrayList<>();
                            if (resInfo.getCurrentTags() != null) {
                                tagsList.addAll(resInfo.getCurrentTags().values());
                            }

                            // 权限
                            Set<String> allowedUserSet = new HashSet<>();
                            if (cn.hutool.core.util.StrUtil.isNotBlank(resInfo.getOwnerId())) {
                                allowedUserSet.add(resInfo.getOwnerId());
                            }
                            if (resInfo.getSpecifiedUsersGrantedActions() != null) {
                                allowedUserSet.addAll(resInfo.getSpecifiedUsersGrantedActions().keySet());
                            }

                            SearchIndexEntity entity = SearchIndexEntity.builder()
                                    .id(SearchIndexEntity.generateEsId(actualResourceType, doc.getResourceId()))
                                    .resourceId(doc.getResourceId())
                                    .resourceType(actualResourceType)
                                    .title(resInfo.getResourceName())
                                    .content(doc.getContent()) // 文档的正文
                                    .allowedUsers(new ArrayList<>(allowedUserSet))
                                    .allowedGroups(new ArrayList<>())
                                    .tags(tagsList)
                                    .build();

                            batchEntities.add(entity);
                        }
                    } catch (Exception e) {
                        log.warn("跑批组装单个文档失败, resourceId: {}", doc.getResourceId());
                    }
                }

                // 3. 批量写入 ES
                if (!batchEntities.isEmpty()) {
                    this.saveOrUpdateBatch(batchEntities);
                    totalProcessed += batchEntities.size();
                }

                if (page >= pageR.getData().getTotalPage()) {
                    break;
                }
                page++;
            }
        } catch (Exception e) {
            log.error("文档索引初始化发生异常", e);
        } finally {
            SecurityContextHolder.remove();
            log.info("============== 文档索引初始化完毕，耗时: {}ms，共插入: {} 条 ==============",
                    System.currentTimeMillis() - start, totalProcessed);
        }
    }

    @Override
    public void rebuildNoteIndex() {
        // 这里留作占位，逻辑和 rebuildDocumentIndex 几乎一模一样。
        // 唯一的区别是把 remoteDocumentService 换成去调 remoteNoteService 拉取笔记的纯文本
        log.info("============== 开始执行【笔记】全量索引重建 ==============");
        // TODO: 参考 Document 的逻辑，拉取 Note 的纯文本并去 Resource 查权限落库
    }
}