# WisePenCloud + WisePenView 关键词全文检索实施计划

## 1. 目标与范围
- 在现有架构下落地**传统关键词匹配全文检索**，支持按关键词检索：
  - 文档正文（`document_content.rawText`）
  - 笔记正文（`note_documents.plainText`）
  - 资源元数据（资源名、标签名、类型等）
- 保留现有权限模型（个人空间/小组空间、ACL、标签过滤），检索结果必须先过权限筛选。
- 目标是“可上线的第一版”：先实现高可用、可解释、可扩展，再迭代相关性策略。

## 2. 当前代码基线（已确认）
- `WisePenCloud` 为后端微服务（Mongo + Kafka），资源列表主入口是：
  - `wisepen-resource-service/.../controller/ResourceItemController.java` → `GET /resource/item/listResources`
  - `ResourceServiceImpl.listResources(...)`
  - `CustomResourceItemRepository.findAccessibleResources(...)`
- 文本来源已具备：
  - 笔记：`note_documents.plainText`（在 `NoteVersionServiceImpl` 的 FULL 快照更新）
  - 文档：`document_content.rawText`（文档解析后写入，且会发布 `TOPIC_DOCUMENT_READY`）
- `WisePenView` 前端资源列表调用链：
  - `src/components/Drive/FlatDrive/FileFilter`（筛选 UI）
  - `src/components/Drive/FlatDrive/FileList`（拉取列表）
  - `src/services/Resource/ResourceServices.impl.ts`（请求 `/resource/item/listResources`）

## 3. 总体方案（推荐）
采用“**资源服务统一检索 + 内容索引聚合**”方案：
1. 在 `resource-service` 新增检索索引集合（如 `wisepen_resource_search_index`），统一持有可检索文本（资源元数据 + 文档/笔记正文摘要）。
2. 通过 Kafka 订阅内容变更事件同步索引：
   - 订阅 `TOPIC_NOTE_SNAPSHOT`（仅 FULL 快照更新 plainText）
   - 订阅 `TOPIC_DOCUMENT_READY`（更新文档正文）
3. 在 `listResources` 增加可选 `keyword` 参数，在权限过滤后叠加关键词匹配与排序。
4. 前端在云盘筛选区增加关键词输入框，透传 `keyword` 到现有列表接口。

> 这样可以避免跨服务实时 Join，复用现有权限逻辑，且后续可平滑升级为 ES/Lucene。

## 4. 详细改造点（按仓库）

### 4.1 WisePenCloud（后端）

#### A. API 与控制层（修改）
1. `wisepen-resource-service/.../controller/ResourceItemController.java`
   - `listResources` 新增 `@RequestParam(value = "keyword", required = false) String keyword`
   - 透传到 service 层。
2. `wisepen-resource-service/.../service/IResourceService.java`
   - `listResources(...)` 方法签名增加 `keyword`。
3. `wisepen-resource-service/.../service/impl/ResourceServiceImpl.java`
   - 将 `keyword` 传入仓储查询逻辑。
   - 返回结果中可追加匹配片段字段（可选：`matchSnippet`）供前端高亮。

#### B. 检索仓储与索引（新增 + 修改）
1. **新增**实体与仓储（resource-service）：
   - `domain/entity/ResourceSearchIndexEntity.java`
   - `repository/ResourceSearchIndexRepository.java`
   - `repository/CustomResourceSearchRepository.java`（MongoTemplate 构建关键词查询）
2. **修改** `CustomResourceItemRepository.java`
   - 在原有 ACL + 标签 + 类型过滤基础上，支持 `keyword` 条件（资源元数据）。
   - 对接 `resource_search_index` 做正文匹配（两阶段：先权限过滤候选，再关键词筛选/排序）。
3. **新增** Mongo 索引初始化（如 `config/SearchIndexMongoConfig.java`）
   - `resourceId` 唯一索引
   - 文本字段索引（resourceName/tagNames/content）
   - 高频过滤字段索引（ownerId/resourceType/groupId）

#### C. 内容同步链路（新增）
1. `wisepen-resource-service/.../mq` 下新增消费者：
   - `NoteSnapshotIndexConsumer.java`（消费 `TOPIC_NOTE_SNAPSHOT`，FULL 快照更新 note 文本索引）
   - `DocumentReadyIndexConsumer.java`（消费 `TOPIC_DOCUMENT_READY`，更新 document 文本索引）
2. **修改** `wisepen-resource-service/.../constant/MqTopicConstants.java`
   - 复用或补充检索相关 topic 常量（必要时）。
3. **修改** `wisepen-resource-service/wisepen-resource-biz/pom.xml`
   - 增加 `wisepen-note-api`、`wisepen-document-api` 依赖（用于消息模型/常量）。

#### D. 资源生命周期一致性（修改）
1. `ResourceServiceImpl.renameResource/updateResourceTags/softRemoveResources/hardRemoveResources`
   - 同步更新或删除检索索引中的元数据与可见状态。
2. 物理删除消费链路（已有 `TOPIC_RESOURCE_PHYSICAL_DESTROY`）补充索引清理，避免脏数据。

### 4.2 WisePenView（前端）

#### A. 类型与服务层（修改）
1. `src/services/Resource/index.type.ts`
   - `GetUserResourcesRequest`、`GetGroupResourceRequest` 增加 `keyword?: string`
2. `src/services/Resource/ResourceServices.impl.ts`
   - 请求参数拼装时透传 `keyword`。

#### B. 筛选 UI 与调用点（修改）
1. `src/components/Drive/FlatDrive/FileFilter/index.type.ts`
   - `FileFilterValue` 增加 `keyword: string`
2. `src/components/Drive/FlatDrive/FileFilter/index.tsx`
   - 新增关键词输入框（建议 `Input.Search` + 300ms 防抖）
3. `src/components/Drive/FlatDrive/index.tsx`
   - 默认筛选对象增加 `keyword`
4. `src/components/Drive/FlatDrive/FileList/index.tsx`
   - 请求参数增加 `keyword`
   - （可选）显示命中片段/高亮关键词

## 5. 交付阶段拆分

### Phase 1（最小可用）
- 后端 `listResources` 增加 `keyword`，先支持元数据匹配（资源名/标签名/类型）。
- 前端 FlatDrive 增加搜索输入并联调。

### Phase 2（正文检索）
- 新增 `resource_search_index` 与 Kafka 消费同步。
- 实现文档正文 + 笔记正文关键词匹配。

### Phase 3（相关性与体验优化）
- 结果排序优化（标题命中 > 标签命中 > 正文命中，命中次数/最近更新时间加权）。
- 命中片段返回与高亮展示。
- 分页性能优化与慢查询观测。

## 6. 验收标准（Definition of Done）
1. 输入关键词后，可检索到仅正文命中的文档/笔记。
2. 元数据命中（标题/标签）可正确召回并排序靠前。
3. 个人/小组权限不泄露：无权限资源不得被检索到。
4. 删除/重命名/改标签后，检索结果在可接受延迟内一致。
5. 空关键词时，行为与当前 `listResources` 完全一致。

## 7. 风险与规避
- **跨服务数据一致性**：采用事件驱动 + 幂等更新（resourceId 作为唯一键）。
- **大文本性能**：入索引时做长度截断与清洗；查询走索引字段，避免全表 regex。
- **中文分词效果**：第一版采用关键词匹配；后续可按需要接入更强分词器/ES。
- **旧数据缺索引**：提供一次性回填任务（离线脚本或受控管理接口）。

