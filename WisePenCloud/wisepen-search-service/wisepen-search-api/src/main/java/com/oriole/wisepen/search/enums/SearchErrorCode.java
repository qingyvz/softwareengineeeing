package com.oriole.wisepen.search.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 搜索服务专属错误码
 */
@Getter
@AllArgsConstructor
public enum SearchErrorCode {

    ES_CONNECTION_ERROR("SEARCH_5001", "Elasticsearch 集群连接异常"),
    ES_INDEX_REBUILD_RUNNING("SEARCH_5002", "全量索引重建任务正在运行中，请勿重复触发"),
    ES_QUERY_BUILD_ERROR("SEARCH_5003", "搜索查询条件构建失败"),
    ES_SYNC_MESSAGE_ERROR("SEARCH_5004", "处理 Kafka 同步消息解析失败");

    private final String code;
    private final String message;
}