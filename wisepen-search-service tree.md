wisepen-search-service
│  pom.xml
│
├─wisepen-search-api
│  │  pom.xml
│  │
│  └─src
│     └─main
│        └─java
│           └─com
│              └─oriole
│                 └─wisepen
│                    └─search
│                       ├─constant
│                       │      SearchConstants.java
│                       │      SearchValidationMsg.java
│                       │
│                       ├─domain
│                       │  ├─base
│                       │  │      SearchHitBase.java
│                       │  │
│                       │  ├─dto
│                       │  │  ├─req
│                       │  │  │      SearchQueryReqDTO.java
│                       │  │  │
│                       │  │  └─res
│                       │  │         SearchHitItemResDTO.java
│                       │  │         SearchResultResDTO.java
│                       │  │
│                       │  └─mq
│                       │         // 说明：搜索服务主要是消息的消费者，通常不在 api 包定义 mq 发送实体。
│                       │         // 但如果未来有类似“索引重建完成”的广播，可放在此处。
│                       │
│                       ├─enums
│                       │      SearchTargetType.java
│                       │      SearchSortBy.java
│                       │
│                       └─feign
│                              RemoteSearchService.java
│
└─wisepen-search-biz
   │  pom.xml
   │
   └─src
      └─main
         ├─java
         │  └─com
         │     └─oriole
         │        └─wisepen
         │           └─search
         │              │  SearchApplication.java
         │              │
         │              ├─config
         │              │      ElasticsearchConfiguration.java
         │              │      SearchProperties.java
         │              │
         │              ├─controller
         │              │      InternalSearchController.java
         │              │      SearchController.java
         │              │
         │              ├─domain
         │              │  └─entity
         │              │         SearchIndexEntity.java
         │              │
         │              ├─exception
         │              │      SearchErrorCode.java
         │              │
         │              ├─mq
         │              │      DocumentParseTaskConsumer.java
         │              │      NoteSnapshotConsumer.java
         │              │      ResourceEventConsumer.java
         │              │
         │              ├─repository
         │              │      SearchIndexRepository.java
         │              │
         │              ├─service
         │              │  │  ISearchQueryService.java
         │              │  │  ISearchSyncService.java
         │              │  │
         │              │  └─impl
         │              │         SearchQueryServiceImpl.java
         │              │         SearchSyncServiceImpl.java
         │              │
         │              └─task
         │                     IndexRebuildTask.java  // 用于处理历史数据全量同步到 ES 的跑批任务
         │
         └─resources
                bootstrap.yml