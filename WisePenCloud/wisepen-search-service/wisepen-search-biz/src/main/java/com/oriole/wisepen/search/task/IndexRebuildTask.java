package com.oriole.wisepen.search.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 搜索引擎索引重建/补偿跑批任务
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class IndexRebuildTask {

    // private final RemoteResourceService remoteResourceService; // FeignClient
    // private final ISearchSyncService searchSyncService;

    /**
     * 每天凌晨 3 点执行，补偿 ES 丢失的数据
     * 实际生产中建议使用 XXL-JOB 等分布式调度框架，这里用 @Scheduled 占位演示
     */
    @Scheduled(cron = "0 0 3 * * ?")
    public void rebuildIndex() {
        log.info("开始执行全量索引补偿任务...");
        long start = System.currentTimeMillis();

        try {
            // 1. 通过 Feign 拉取最新的变更数据或者全量数据
            // List<ResourceDTO> dbList = remoteResourceService.fetchRecentChanges();

            // 2. 转换为 SearchIndexEntity
            // List<SearchIndexEntity> esEntities = convertToEsEntities(dbList);

            // 3. 批量写入 ES
            // searchSyncService.saveOrUpdateBatch(esEntities);

        } catch (Exception e) {
            log.error("全量索引补偿任务执行失败", e);
        } finally {
            log.info("全量索引补偿任务执行完毕，耗时: {}ms", System.currentTimeMillis() - start);
        }
    }
}
