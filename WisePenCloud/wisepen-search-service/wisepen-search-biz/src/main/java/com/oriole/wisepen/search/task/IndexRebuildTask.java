package com.oriole.wisepen.search.task;

import com.oriole.wisepen.search.service.ISearchSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 搜索引擎索引全量重建/补偿跑批任务
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class IndexRebuildTask {

    private final ISearchSyncService searchSyncService;

    @Scheduled(cron = "0 0 3 * * ?") // 每天凌晨 3 点跑批
    public void rebuildIndex() {
        log.info("定时任务触发：凌晨 ES 索引对账/补偿...");
        searchSyncService.rebuildDocumentIndex();
        searchSyncService.rebuildNoteIndex();
    }
}