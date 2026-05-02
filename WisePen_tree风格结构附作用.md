# WisePen tree /f 风格结构（附作用）

```text
D:\fudan\4_semester\SoftwareEngineering
├─ WisePenCloud                                              # 后端微服务主仓
│  ├─ wisepen-common                                         # 通用能力（异常/鉴权上下文/响应体/拦截）
│  │  └─ src\main\java\com\oriole\wisepen\common
│  │     ├─ core\domain\R.java                               # 统一响应包装
│  │     ├─ core\domain\PageResult.java                      # 统一分页模型
│  │     ├─ core\handler\GlobalExceptionHandler.java         # 全局异常处理
│  │     ├─ core\context\SecurityContextHolder.java          # 当前登录用户上下文
│  │     ├─ security\aspect\SecurityAspect.java              # 登录/角色鉴权切面
│  │     └─ web\interceptor\FeignRequestInterceptor.java     # Feign 透传上下文
│  │
│  ├─ wisepen-common-log                                     # 操作日志公共模块
│  │  └─ src\main\java\com\oriole\wisepen\common\log
│  │     ├─ aspect\LogAspect.java                            # 注解日志切面
│  │     └─ service\AsyncLogService.java                     # 异步日志落地
│  │
│  ├─ wisepen-user-service                                   # 用户/认证/小组/钱包服务
│  │  ├─ wisepen-user-biz\src\main\java\com\oriole\wisepen\user
│  │  │  ├─ UserApplication.java                             # 服务启动入口
│  │  │  ├─ controller\AuthController.java                   # 登录注册登出重置密码
│  │  │  ├─ controller\UserController.java                   # 用户资料接口
│  │  │  ├─ controller\GroupController.java                  # 小组管理接口
│  │  │  ├─ controller\GroupMemberController.java            # 小组成员管理接口
│  │  │  ├─ controller\WalletController.java                 # 钱包与代币接口
│  │  │  ├─ service\AuthService.java                         # 认证核心逻辑
│  │  │  ├─ service\impl\UserServiceImpl.java                # 用户业务实现
│  │  │  ├─ service\impl\GroupServiceImpl.java               # 小组业务实现
│  │  │  ├─ service\impl\GroupMemberServiceImpl.java         # 成员业务实现
│  │  │  ├─ service\impl\WalletServiceImpl.java              # 钱包业务实现
│  │  │  ├─ mapper\*.java                                    # MyBatis DAO 层
│  │  │  ├─ domain\entity\*.java                             # 用户域实体
│  │  │  ├─ strategy\VerificationStrategyFactory.java        # 验证策略选择
│  │  │  ├─ strategy\impl\EmailVerificationStrategy.java     # 邮箱验证策略
│  │  │  ├─ strategy\impl\FudanUISVerificationStrategy.java  # UIS 验证策略
│  │  │  └─ consumer\TokenConsumptionConsumer.java           # 代币消费事件消费
│  │  └─ wisepen-user-api\src\main\java\...\api
│  │     ├─ feign\RemoteUserService.java                     # 对外 Feign 接口
│  │     ├─ domain\dto\req\*.java                            # 请求 DTO
│  │     └─ domain\dto\res\*.java                            # 响应 DTO
│  │
│  ├─ wisepen-resource-service                               # 资源/标签/ACL 核心服务
│  │  ├─ wisepen-resource-biz\src\main\java\com\oriole\wisepen\resource
│  │  │  ├─ ResPermissionApplication.java                    # 服务启动入口
│  │  │  ├─ controller\ResourceItemController.java           # 资源查询与管理主入口
│  │  │  ├─ controller\ResourceTagController.java            # 标签管理接口
│  │  │  ├─ controller\GroupResConfigController.java         # 组资源配置接口
│  │  │  ├─ service\impl\ResourceServiceImpl.java            # 资源聚合核心（标签+权限+列表）
│  │  │  ├─ service\impl\TagServiceImpl.java                 # 标签树业务
│  │  │  ├─ service\impl\GroupResServiceImpl.java            # 小组资源规则
│  │  │  ├─ repository\CustomResourceItemRepository.java     # 复杂筛选查询
│  │  │  ├─ repository\ResourceItemRepository.java           # 资源仓储
│  │  │  ├─ repository\TagRepository.java                    # 标签仓储
│  │  │  ├─ repository\GroupResConfigRepository.java         # 组配置仓储
│  │  │  ├─ mq\AclRecalculateConsumer.java                   # ACL 重算消费者
│  │  │  ├─ mq\KafkaEventPublisherImpl.java                  # 资源事件发布
│  │  │  ├─ event\TagChangedEvent.java                       # 标签变化事件
│  │  │  ├─ domain\entity\*.java                             # 资源域实体
│  │  │  └─ task\ResourceGcTask.java                         # 资源垃圾回收
│  │  └─ wisepen-resource-api\src\main\java\...\resource
│  │     ├─ feign\RemoteResourceService.java                 # 对外 Feign 接口
│  │     ├─ domain\dto\req\*.java                            # 资源请求 DTO
│  │     ├─ domain\dto\res\*.java                            # 资源响应 DTO
│  │     └─ enums\*.java                                     # 资源业务枚举
│  │
│  ├─ wisepen-note-service                                   # 笔记协同与版本服务
│  │  ├─ wisepen-note-biz\src\main\java\com\oriole\wisepen\note
│  │  │  ├─ NoteApplication.java                             # 服务启动入口
│  │  │  ├─ controller\NoteController.java                   # 笔记接口（创建/详情/版本）
│  │  │  ├─ controller\OpLogController.java                  # 笔记操作日志接口
│  │  │  ├─ service\impl\NoteServiceImpl.java                # 笔记主业务
│  │  │  ├─ service\impl\NoteVersionServiceImpl.java         # 版本业务
│  │  │  ├─ service\impl\NoteOperationLogServiceImpl.java    # 操作日志业务
│  │  │  ├─ repository\NoteDocumentRepository.java           # 笔记正文仓储
│  │  │  ├─ repository\NoteVersionRepository.java            # 版本仓储
│  │  │  ├─ repository\NoteOperationLogRepository.java       # 操作日志仓储
│  │  │  ├─ consumer\NoteSnapshotConsumer.java               # 快照事件消费
│  │  │  ├─ consumer\NoteOperationLogConsumer.java           # 操作日志事件消费
│  │  │  └─ domain\entity\*.java                             # 笔记域实体
│  │  └─ wisepen-note-api\src\main\java\...\note\api
│  │     ├─ feign\RemoteNoteService.java                     # 对外 Feign 接口
│  │     └─ domain\mq\*.java                                 # 笔记事件消息体
│  │
│  ├─ wisepen-document-service                               # 文档上传、解析、预览服务
│  │  ├─ wisepen-document-biz\src\main\java\com\oriole\wisepen\document
│  │  │  ├─ DocumentApplication.java                         # 服务启动入口
│  │  │  ├─ controller\DocumentController.java               # 文档主接口（上传/状态/预览）
│  │  │  ├─ service\impl\DocumentServiceImpl.java            # 文档生命周期业务
│  │  │  ├─ service\impl\DocumentFileServiceImpl.java        # 文档文件处理
│  │  │  ├─ service\impl\DocumentPreviewServiceImpl.java     # PDF 预览处理
│  │  │  ├─ repository\DocumentInfoRepository.java           # 文档信息仓储
│  │  │  ├─ repository\DocumentContentRepository.java        # 文档内容仓储
│  │  │  ├─ repository\DocumentPdfMetaRepository.java        # PDF 元信息仓储
│  │  │  ├─ consumer\FileUploadedConsumer.java               # 上传完成事件消费
│  │  │  ├─ consumer\DocumentConversionAndParseConsumer.java # 转换解析事件消费
│  │  │  ├─ mq\KafkaDocumentEventPublisher.java              # 文档事件发布
│  │  │  ├─ util\*.java                                      # 水印/PDF 工具
│  │  │  └─ domain\entity\*.java                             # 文档域实体
│  │  └─ wisepen-document-api\src\main\java\...\document\api
│  │     ├─ domain\dto\req\DocumentUploadInitRequest.java    # 上传初始化请求
│  │     ├─ domain\dto\res\DocumentUploadInitResponse.java   # 上传初始化响应
│  │     └─ domain\mq\*.java                                 # 文档事件消息体
│  │
│  ├─ wisepen-file-storage-service                           # 对象存储抽象服务
│  │  ├─ wisepen-file-storage-biz\src\main\java\...\storage
│  │  │  ├─ FileStorageApplication.java                      # 服务启动入口
│  │  │  ├─ controller\StorageController.java                # 上传接口
│  │  │  ├─ controller\InternalStorageController.java        # 内部存储接口
│  │  │  ├─ service\impl\StorageServiceImpl.java             # 存储业务实现
│  │  │  ├─ strategy\StorageProvider.java                    # 存储策略接口
│  │  │  ├─ strategy\StorageManager.java                     # 存储策略路由器
│  │  │  ├─ strategy\aliyun\AliyunOssProvider.java           # 阿里 OSS 实现
│  │  │  ├─ mapper\StorageRecordMapper.java                  # 存储记录 DAO
│  │  │  ├─ mq\KafkaStorageEventPublisher.java               # 存储事件发布
│  │  │  └─ domain\entity\*.java                             # 存储域实体
│  │  └─ wisepen-file-storage-api\src\main\java\...\api
│  │     ├─ feign\RemoteStorageService.java                  # 对外 Feign 接口
│  │     └─ domain\dto\*.java                                # 上传/STS DTO
│  │
│  ├─ wisepen-system-service                                 # 系统能力服务（邮件/反馈/日志）
│  │  ├─ wisepen-system-biz\src\main\java\...\system
│  │  │  ├─ SystemApplication.java                           # 服务启动入口
│  │  │  ├─ controller\MailController.java                   # 邮件接口
│  │  │  ├─ controller\UserFeedbackController.java           # 反馈接口
│  │  │  ├─ controller\RemoteLogController.java              # 远程日志接口
│  │  │  ├─ service\impl\SysMailServiceImpl.java             # 邮件业务
│  │  │  ├─ service\impl\FeedbackServiceImpl.java            # 反馈业务
│  │  │  ├─ service\impl\SysOperLogServiceImpl.java          # 操作日志业务
│  │  │  ├─ mapper\*.java                                    # 系统 DAO
│  │  │  └─ domain\entity\*.java                             # 系统域实体
│  │  └─ wisepen-system-api\src\main\java\...\api
│  │     ├─ feign\RemoteMailService.java                     # 远程邮件接口
│  │     └─ feign\RemoteLogService.java                      # 远程日志接口
│  │
│  └─ wisepen-fudan-extension-service                        # 复旦 UIS 扩展服务
│     ├─ wisepen-fudan-extension-biz\src\main\java\...\fudan
│     │  ├─ FudanExtensionApplication.java                   # 服务启动入口
│     │  ├─ controller\UISTaskController.java                # UIS 任务接口
│     │  ├─ consumer\UISAuthRequestConsumer.java             # UIS 鉴权事件消费
│     │  └─ cache\RedisCacheManager.java                     # 缓存管理
│     └─ wisepen-fudan-extension-api\src\main\java\...\api
│        └─ feign\RemoteFudanExtensionService.java           # 对外 Feign 接口
│
└─ WisePenView                                               # 前端 React + TypeScript
   └─ src
      ├─ main.tsx                                            # 前端入口
      ├─ App.tsx                                             # 全局 Provider/主题/鉴权同步
      ├─ router.tsx                                          # 路由定义
      │
      ├─ layouts                                             # 页面壳层
      │  ├─ SystemLayout.tsx                                 # 系统内页主壳
      │  ├─ HomeLayout.tsx                                   # 首页壳
      │  └─ AuthLayout.tsx                                   # 认证页壳
      │
      ├─ views                                               # 页面级组件
      │  ├─ drive\Drive\index.tsx                            # 云盘主页面
      │  ├─ note\index.tsx                                   # 笔记页面
      │  ├─ pdf\PdfPreview\index.tsx                         # PDF 预览页面
      │  ├─ group\MyGroup\index.tsx                          # 我的小组页面
      │  ├─ group\GroupDetail\index.tsx                      # 小组详情页面
      │  ├─ profile\Account\index.tsx                        # 账户页面
      │  ├─ profile\Usage\index.tsx                          # 用量页面
      │  ├─ auth\Login\index.tsx                             # 登录页面
      │  ├─ auth\Register\index.tsx                          # 注册页面
      │  └─ error\*.tsx                                      # 错误页
      │
      ├─ contexts\ServicesContext                            # 服务依赖注入
      │  ├─ ServicesProvider.tsx                             # 服务上下文 Provider
      │  ├─ registry.impl.ts                                 # 真实服务装配中心
      │  ├─ hooks.ts                                         # useXxxService hooks
      │  └─ context.ts                                       # Context 定义
      │
      ├─ services                                            # API 服务层
      │  ├─ Auth\AuthServices.impl.ts                        # 认证 API
      │  ├─ User\UserServices.impl.ts                        # 用户 API
      │  ├─ Group\GroupServices.impl.ts                      # 小组 API
      │  ├─ Wallet\WalletServices.impl.ts                    # 钱包 API
      │  ├─ Quota\QuotaServices.impl.ts                      # 配额 API
      │  ├─ Resource\ResourceServices.impl.ts                # 资源 API
      │  ├─ Tag\TagServices.impl.ts                          # 标签 API
      │  ├─ Sticker\StickerServices.impl.ts                  # 标签模板 API
      │  ├─ Folder\FolderServices.impl.ts                    # 文件夹 API
      │  ├─ Document\DocumentServices.impl.ts                # 文档 API
      │  ├─ Note\NoteServices.impl.ts                        # 笔记 API
      │  ├─ Chat\ChatServices.impl.ts                        # 聊天 API
      │  ├─ Image\ImageServices.impl.ts                      # 图像上传 API
      │  └─ cacheRegistry.ts                                 # 服务缓存统一清理
      │
      ├─ session                                             # 实时会话封装
      │  ├─ chat\useChatSession.ts                           # 聊天流式会话封装
      │  └─ note
      │     ├─ useNoteSession.ts                             # 笔记协同会话（Yjs）
      │     ├─ WisepenProvider.ts                            # 协同 Provider
      │     └─ NoteStatusObserver.ts                         # 协同状态观察
      │
      ├─ store\zustand                                       # 全局状态
      │  ├─ useDrivePreferencesStore.ts                      # 云盘视图偏好
      │  ├─ useCurrentChatSessionStore.ts                    # 当前聊天会话
      │  ├─ useNewChatSessionStore.ts                        # 新建会话过渡态
      │  ├─ useChatPanelStore.ts                             # 聊天面板折叠状态
      │  ├─ useNoteSelectionStore.ts                         # 选中文本上下文
      │  ├─ useRecentFilesStore.ts                           # 最近文件
      │  ├─ useTreeDriveCwdStore.ts                          # 树视图当前目录
      │  ├─ clearAllStores.ts                                # 清空所有状态
      │  └─ sessionStorage.ts                                # 持久化存储适配
      │
      ├─ hooks                                               # 业务 hooks
      │  ├─ useClickFile.ts                                  # 点击文件后的统一动作
      │  ├─ useAppMessage.ts                                 # 消息提示封装
      │  └─ drive
      │     ├─ useTreeDrive.ts                               # 树形云盘状态机
      │     ├─ useTreeDriveDrop.ts                           # 树形拖拽逻辑
      │     └─ treeRowDataUtil.ts                            # 树行数据转换
      │
      ├─ components                                          # 业务组件层
      │  ├─ Drive                                            # 云盘组件（FlatDrive/TreeDrive/Modals）
      │  ├─ Note                                             # 笔记组件（编辑器/标题/信息条）
      │  ├─ ChatPanel                                        # 聊天组件（输入/消息/模型）
      │  ├─ Group                                            # 小组组件（成员表/弹窗）
      │  ├─ Sidebar                                          # 侧边栏与会话导航
      │  ├─ Account                                           # 账号与认证组件
      │  ├─ Wallet                                           # 钱包组件
      │  ├─ Pdf                                              # PDF 组件
      │  └─ Common                                           # 通用展示组件
      │
      └─ utils                                               # 通用工具
         ├─ Axios.ts                                         # HTTP 客户端与拦截器
         ├─ response.ts                                      # 响应校验
         ├─ parseErrorMessage.ts                             # 错误文案提取
         ├─ apiServerAddr.ts                                 # API 地址解析
         ├─ authSync.ts                                      # 鉴权状态同步
         ├─ serializeRepeatKeyQuery.ts                       # 重复 query 序列化
         ├─ openedResourceRoute.ts                           # 资源路由拼装
         ├─ path.ts                                          # 路径工具
         ├─ time.ts                                          # 时间工具
         ├─ format.ts                                        # 格式化工具
         └─ computeFileMd5.ts                                # 文件 MD5 计算
```

