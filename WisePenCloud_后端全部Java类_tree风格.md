# WisePenCloud 后端全部 Java 类清单（tree /f 风格）

> 说明：覆盖 `WisePenCloud` 下全部 `src/main/java/**/*.java`，并改写为 tree 风格展示（文件后附作用）。

```text
WisePenCloud
├─ wisepen-common
│  └─ src\main\java\com\oriole\wisepen\common
│     ├─ web\interceptor\HeaderInterceptor.java                        # Web 请求头拦截与上下文处理
│     ├─ web\interceptor\FeignRequestInterceptor.java                  # Feign 调用头透传
│     ├─ gray\GrayServiceInstanceListSupplier.java                     # 灰度实例列表选择器
│     ├─ gray\GrayLoadBalancerConfiguration.java                       # 灰度负载配置
│     ├─ gray\DeveloperIsolationAutoConfiguration.java                 # 开发者隔离自动配置
│     ├─ config\WisepenWebAutoConfiguration.java                       # Web 自动配置
│     ├─ config\RedisConfiguration.java                                # Redis 配置
│     ├─ config\JacksonConfiguration.java                              # Jackson 序列化配置
│     ├─ config\MybatisPlusMetaObjectHandler.java                      # MyBatis 元对象填充处理
│     ├─ config\GlobalGrayLoadBalancerAutoConfiguration.java           # 全局灰度 LB 自动配置
│     ├─ config\KafkaErrorHandlerConfiguration.java                    # Kafka 错误处理配置
│     ├─ config\FeignConfiguration.java                                # Feign 配置
│     ├─ security\aspect\SecurityAspect.java                           # 鉴权切面
│     ├─ security\annotation\CheckRole.java                            # 角色校验注解
│     ├─ security\exception\PermissionException.java                   # 权限异常
│     ├─ security\annotation\CheckLogin.java                           # 登录校验注解
│     ├─ security\exception\PermissionErrorCode.java                   # 权限错误码定义
│     ├─ core\handler\GlobalExceptionHandler.java                      # 全局异常处理器
│     ├─ core\constant\SecurityConstants.java                          # 安全常量
│     ├─ core\constant\CommonConstants.java                            # 通用常量
│     ├─ core\context\SecurityContextHolder.java                       # 安全上下文持有器
│     ├─ core\context\GrayContextHolder.java                           # 灰度上下文持有器
│     ├─ core\exception\ServiceException.java                          # 业务异常基类
│     ├─ core\exception\IErrorCode.java                                # 错误码接口
│     ├─ core\domain\R.java                                            # 统一响应体
│     ├─ core\domain\PageResult.java                                   # 分页响应体
│     ├─ core\domain\enums\ResultCode.java                             # 通用结果码
│     ├─ core\domain\enums\GroupRoleType.java                          # 组角色枚举
│     ├─ core\domain\enums\BusinessType.java                           # 业务类型枚举
│     ├─ core\domain\enums\GroupType.java                              # 组类型枚举
│     ├─ core\domain\enums\IdentityType.java                           # 身份类型枚举
│     ├─ core\domain\enums\list\SortDirectionEnum.java                 # 排序方向枚举
│     └─ core\domain\enums\list\QueryLogicEnum.java                    # 查询逻辑枚举
├─ wisepen-common-log
│  └─ src\main\java\com\oriole\wisepen\common\log
│     ├─ service\AsyncLogService.java                                  # 异步日志服务
│     ├─ config\LogAutoConfiguration.java                              # 日志自动配置
│     ├─ aspect\LogAspect.java                                         # 日志切面
│     └─ annotation\Log.java                                           # 日志注解
├─ wisepen-resource-service
│  ├─ wisepen-resource-api\src\main\java\com\oriole\wisepen\resource
│  │  ├─ constant\ResourceValidationMsg.java                           # 资源校验文案常量
│  │  ├─ constant\ResourceConstants.java                               # 资源常量
│  │  ├─ constant\MqTopicConstants.java                                # 资源域 MQ Topic 常量
│  │  ├─ feign\RemoteResourceService.java                              # 资源服务 Feign 对外接口
│  │  ├─ enums\ResourceType.java                                       # 资源类型枚举
│  │  ├─ enums\ResourceSortBy.java                                     # 资源排序枚举
│  │  ├─ enums\ResourceAction.java                                     # 资源动作权限枚举
│  │  ├─ enums\ResourceAccessRole.java                                 # 资源访问角色枚举
│  │  ├─ enums\FileOrganizationLogic.java                              # 文件组织逻辑枚举
│  │  ├─ enums\AclGrantMode.java                                       # ACL 授权模式枚举
│  │  ├─ domain\mq\ResourceDeletedMessage.java                         # 资源删除消息体
│  │  ├─ domain\mq\AclRecalculateMessage.java                          # ACL 重算消息体
│  │  ├─ domain\base\ResourceItemInfoBase.java                         # 资源基础模型
│  │  ├─ domain\base\TagSpaceBase.java                                 # 标签空间基础模型
│  │  ├─ domain\base\TagInfoBase.java                                  # 标签基础模型
│  │  ├─ domain\dto\ResourceUpdateReqDTO.java                          # 资源更新 DTO
│  │  ├─ domain\dto\ResourceInfoGetReqDTO.java                         # 资源查询 DTO
│  │  ├─ domain\dto\ResourceCreateReqDTO.java                          # 资源创建 DTO
│  │  ├─ domain\dto\ResourceCheckPermissionResDTO.java                 # 权限校验响应 DTO
│  │  ├─ domain\dto\ResourceCheckPermissionReqDTO.java                 # 权限校验请求 DTO
│  │  ├─ domain\dto\res\TagTreeResponse.java                           # 标签树响应 DTO
│  │  ├─ domain\dto\res\ResourceItemResponse.java                      # 资源项响应 DTO
│  │  ├─ domain\dto\res\GroupResConfigResponse.java                    # 组配置响应 DTO
│  │  ├─ domain\dto\req\TagUpdateRequest.java                          # 标签更新请求 DTO
│  │  ├─ domain\dto\req\TagMoveRequest.java                            # 标签移动请求 DTO
│  │  ├─ domain\dto\req\TagDeleteRequest.java                          # 标签删除请求 DTO
│  │  ├─ domain\dto\req\TagCreateRequest.java                          # 标签创建请求 DTO
│  │  ├─ domain\dto\req\ResourceUpdateTagsRequest.java                 # 资源标签更新请求 DTO
│  │  ├─ domain\dto\req\ResourceUpdateActionPermissionRequest.java     # 资源动作权限更新请求 DTO
│  │  ├─ domain\dto\req\ResourceRenameRequest.java                     # 资源重命名请求 DTO
│  │  └─ domain\dto\req\GroupResConfigUpdateRequest.java               # 组配置更新请求 DTO
│  └─ wisepen-resource-biz\src\main\java\com\oriole\wisepen\resource
│     ├─ ResPermissionApplication.java                                 # 资源服务启动入口
│     ├─ task\ResourceGcTask.java                                      # 资源垃圾回收任务
│     ├─ controller\ResourceTagController.java                         # 标签相关接口控制器
│     ├─ controller\ResourceItemController.java                        # 资源主接口控制器
│     ├─ controller\InternalResourceItemController.java                # 资源内部接口控制器
│     ├─ controller\GroupResConfigController.java                      # 组资源配置控制器
│     ├─ exception\ResPermissionErrorCode.java                         # 资源权限错误码
│     ├─ config\ResourceProperties.java                                # 资源配置项
│     ├─ mq\KafkaEventPublisherImpl.java                               # Kafka 事件发布实现
│     ├─ mq\IEventPublisher.java                                       # 事件发布接口
│     ├─ mq\AclRecalculateConsumer.java                                # ACL 重算消息消费者
│     ├─ event\TagTrashedEvent.java                                    # 标签回收事件
│     ├─ event\TagDeletedEvent.java                                    # 标签删除事件
│     ├─ event\TagChangedEvent.java                                    # 标签变更事件
│     ├─ service\IGroupResService.java                                 # 组资源服务接口
│     ├─ service\IResourceService.java                                 # 资源服务接口
│     ├─ service\ITagService.java                                      # 标签服务接口
│     ├─ repository\TagRepository.java                                 # 标签仓储
│     ├─ repository\ResourceItemRepository.java                        # 资源仓储
│     ├─ repository\GroupResConfigRepository.java                      # 组配置仓储
│     ├─ repository\CustomResourceItemRepository.java                  # 复杂资源查询仓储
│     ├─ domain\GroupTagBind.java                                      # 组与标签绑定模型
│     ├─ domain\ComputedGroupAcl.java                                  # 计算后的组 ACL 模型
│     ├─ service\impl\TagServiceImpl.java                              # 标签服务实现
│     ├─ service\impl\ResourceServiceImpl.java                         # 资源服务实现
│     ├─ service\impl\GroupResServiceImpl.java                         # 组资源服务实现
│     ├─ domain\entity\TagEntity.java                                  # 标签实体
│     ├─ domain\entity\ResourceItemEntity.java                         # 资源实体
│     └─ domain\entity\GroupResConfigEntity.java                       # 组资源配置实体
├─ wisepen-note-service
│  ├─ wisepen-note-api\src\main\java\com\oriole\wisepen\note\api
│  │  ├─ feign\RemoteNoteService.java                                  # 笔记服务 Feign 对外接口
│  │  ├─ domain\mq\NoteSnapshotMessage.java                            # 笔记快照消息体
│  │  ├─ domain\mq\NoteOperationLogMessage.java                        # 笔记操作日志消息体
│  │  ├─ domain\enums\VersionType.java                                 # 版本类型枚举
│  │  ├─ domain\dto\res\NoteVersionListResponse.java                   # 版本列表响应 DTO
│  │  ├─ domain\dto\res\NoteSnapshotResponse.java                      # 快照响应 DTO
│  │  ├─ domain\dto\res\NoteOperationLogResponse.java                  # 日志响应 DTO
│  │  ├─ domain\dto\res\NoteInfoResponse.java                          # 笔记信息响应 DTO
│  │  ├─ domain\dto\req\NoteVersionQueryRequest.java                   # 版本查询请求 DTO
│  │  ├─ domain\dto\req\NoteCreateRequest.java                         # 笔记创建请求 DTO
│  │  ├─ domain\base\NoteVersionBase.java                              # 版本基础模型
│  │  ├─ domain\base\NoteOperationLogBase.java                         # 日志基础模型
│  │  ├─ domain\base\NoteInfoBase.java                                 # 笔记基础模型
│  │  ├─ constant\NoteValidationMsg.java                               # 笔记校验文案常量
│  │  └─ constant\MqTopicConstants.java                                # 笔记域 MQ Topic 常量
│  └─ wisepen-note-biz\src\main\java\com\oriole\wisepen\note
│     ├─ NoteApplication.java                                          # 笔记服务启动入口
│     ├─ service\INoteVersionService.java                              # 笔记版本服务接口
│     ├─ service\INoteService.java                                     # 笔记服务接口
│     ├─ service\INoteOperationLogService.java                         # 笔记操作日志服务接口
│     ├─ service\impl\NoteVersionServiceImpl.java                      # 版本服务实现
│     ├─ service\impl\NoteServiceImpl.java                             # 笔记服务实现
│     ├─ service\impl\NoteOperationLogServiceImpl.java                 # 操作日志服务实现
│     ├─ repository\NoteVersionRepository.java                         # 笔记版本仓储
│     ├─ repository\NoteOperationLogRepository.java                    # 操作日志仓储
│     ├─ repository\NoteDocumentRepository.java                        # 笔记正文仓储
│     ├─ exception\NoteErrorCode.java                                  # 笔记错误码
│     ├─ domain\entity\NoteVersionEntity.java                          # 笔记版本实体
│     ├─ domain\entity\NoteOperationLogEntity.java                     # 笔记日志实体
│     ├─ domain\entity\NoteInfoEntity.java                             # 笔记信息实体
│     ├─ controller\OpLogController.java                               # 操作日志控制器
│     ├─ controller\NoteController.java                                # 笔记主控制器
│     ├─ controller\InternalNoteController.java                        # 笔记内部控制器
│     ├─ consumer\ResourceDeletedConsumer.java                         # 资源删除消费者
│     ├─ consumer\NoteSnapshotConsumer.java                            # 快照消费者
│     ├─ consumer\NoteOperationLogConsumer.java                        # 操作日志消费者
│     └─ config\NoteProperties.java                                    # 笔记配置
├─ wisepen-document-service
│  ├─ wisepen-document-api\src\main\java\com\oriole\wisepen\document\api
│  │  ├─ constant\MqTopicConstants.java                                # 文档域 MQ Topic 常量
│  │  ├─ constant\DocumentValidationMsg.java                           # 文档校验文案常量
│  │  ├─ constant\DocumentConstants.java                               # 文档常量
│  │  ├─ enums\DocumentStatusEnum.java                                 # 文档状态枚举
│  │  ├─ domain\base\DocumentUploadMeta.java                           # 文档上传元数据基础模型
│  │  ├─ domain\base\DocumentStatus.java                               # 文档状态基础模型
│  │  ├─ domain\base\DocumentInfoBase.java                             # 文档信息基础模型
│  │  ├─ domain\mq\DocumentReadyMessage.java                           # 文档就绪消息体
│  │  ├─ domain\mq\DocumentParseTaskMessage.java                       # 文档解析任务消息体
│  │  ├─ domain\dto\res\DocumentUploadInitResponse.java                # 文档上传初始化响应 DTO
│  │  ├─ domain\dto\res\DocumentInfoResponse.java                      # 文档信息响应 DTO
│  │  └─ domain\dto\req\DocumentUploadInitRequest.java                 # 文档上传初始化请求 DTO
│  └─ wisepen-document-biz\src\main\java\com\oriole\wisepen\document
│     ├─ DocumentApplication.java                                      # 文档服务启动入口
│     ├─ util\WatermarkPreProcessor.java                               # 水印预处理工具
│     ├─ util\WatermarkAppendixBuilder.java                            # 水印附录构建工具
│     ├─ util\PdfContentStreamBuilder.java                             # PDF 内容流构建工具
│     ├─ util\MicroDotCodec.java                                       # 微点编码工具
│     ├─ config\DocumentProperties.java                                # 文档配置项
│     ├─ task\DocumentGcTask.java                                      # 文档垃圾回收任务
│     ├─ consumer\ResourceDeletedConsumer.java                         # 资源删除消费者
│     ├─ consumer\FileUploadedConsumer.java                            # 文件上传完成消费者
│     ├─ consumer\DocumentConversionAndParseConsumer.java              # 文档转换解析消费者
│     ├─ service\IDocumentPreviewService.java                          # 文档预览服务接口
│     ├─ service\IDocumentFileService.java                             # 文档文件服务接口
│     ├─ service\IDocumentService.java                                 # 文档服务接口
│     ├─ mq\KafkaDocumentEventPublisher.java                           # 文档事件发布器
│     ├─ controller\DocumentController.java                            # 文档外部控制器
│     ├─ controller\InternalDocumentController.java                    # 文档内部控制器
│     ├─ repository\DocumentPdfMetaRepository.java                     # PDF 元数据仓储
│     ├─ repository\DocumentInfoRepository.java                        # 文档信息仓储
│     ├─ repository\DocumentContentRepository.java                     # 文档内容仓储
│     ├─ exception\DocumentErrorCode.java                              # 文档错误码
│     ├─ service\impl\DocumentServiceImpl.java                         # 文档服务实现
│     ├─ service\impl\DocumentPreviewServiceImpl.java                  # 文档预览实现
│     ├─ service\impl\DocumentFileServiceImpl.java                     # 文档文件服务实现
│     ├─ domain\entity\DocumentPdfMetaEntity.java                      # 文档 PDF 元数据实体
│     ├─ domain\entity\DocumentInfoEntity.java                         # 文档信息实体
│     └─ domain\entity\DocumentContentEntity.java                      # 文档内容实体
├─ wisepen-file-storage-service
│  ├─ wisepen-file-storage-api\src\main\java\com\oriole\wisepen\file\storage\api
│  │  ├─ feign\RemoteStorageService.java                               # 存储服务 Feign 接口
│  │  ├─ enums\StorageProviderEnum.java                                # 存储提供方枚举
│  │  ├─ enums\StorageStatusEnum.java                                  # 存储状态枚举
│  │  ├─ enums\StorageSceneEnum.java                                   # 存储场景枚举
│  │  ├─ constant\MqTopicConstants.java                                # 存储域 MQ Topic 常量
│  │  ├─ domain\base\UploadUrlBase.java                                # 上传 URL 基础模型
│  │  ├─ domain\base\StorageRecordBase.java                            # 存储记录基础模型
│  │  ├─ domain\dto\StsTokenDTO.java                                   # STS Token DTO
│  │  ├─ domain\dto\StorageRecordDTO.java                              # 存储记录 DTO
│  │  ├─ domain\dto\UploadInitReqDTO.java                              # 上传初始化请求 DTO
│  │  ├─ domain\dto\UploadInitRespDTO.java                             # 上传初始化响应 DTO
│  │  └─ domain\mq\FileUploadedMessage.java                            # 文件上传完成消息体
│  └─ wisepen-file-storage-biz\src\main\java\com\oriole\wisepen\file\storage
│     ├─ task\StorageGcTask.java                                       # 存储垃圾回收任务
│     ├─ strategy\StorageProvider.java                                 # 存储提供者接口
│     ├─ strategy\StorageManager.java                                  # 存储策略调度器
│     ├─ strategy\aliyun\AliyunOssProvider.java                        # 阿里云 OSS 提供者实现
│     ├─ service\IStorageService.java                                  # 存储服务接口
│     ├─ service\impl\StorageServiceImpl.java                          # 存储服务实现
│     ├─ mq\KafkaStorageEventPublisher.java                            # 存储事件发布器
│     ├─ mapper\StorageRecordMapper.java                               # 存储记录 Mapper
│     ├─ mapper\StorageConfigMapper.java                               # 存储配置 Mapper
│     ├─ FileStorageApplication.java                                   # 存储服务启动入口
│     ├─ exception\StorageErrorCode.java                               # 存储错误码
│     ├─ domain\entity\StorageRecordEntity.java                        # 存储记录实体
│     ├─ domain\entity\StorageConfigEntity.java                        # 存储配置实体
│     ├─ config\StorageProperties.java                                 # 存储配置项
│     ├─ customer\FileDeleteConsumer.java                              # 文件删除消费者
│     ├─ controller\StorageController.java                             # 存储对外控制器
│     ├─ controller\InternalStorageController.java                     # 存储内部控制器
│     └─ controller\ExternalStorageController.java                     # 存储外部控制器
├─ wisepen-system-service
│  ├─ wisepen-system-api\src\main\java\com\oriole\wisepen\system\api
│  │  ├─ feign\RemoteMailService.java                                  # 邮件 Feign 接口
│  │  ├─ feign\RemoteLogService.java                                   # 日志 Feign 接口
│  │  ├─ enums\FeedbackType.java                                       # 反馈类型枚举
│  │  ├─ enums\FeedbackStatus.java                                     # 反馈状态枚举
│  │  ├─ domain\dto\SysOperLogDTO.java                                 # 操作日志 DTO
│  │  ├─ domain\dto\MailSendDTO.java                                   # 邮件发送 DTO
│  │  ├─ domain\dto\FeedbackRequest.java                               # 反馈请求 DTO
│  │  └─ constant\MailValidationMessage.java                           # 邮件校验文案常量
│  └─ wisepen-system-biz\src\main\java\com\oriole\wisepen\system
│     ├─ SystemApplication.java                                        # 系统服务启动入口
│     ├─ service\SysOperLogService.java                                # 操作日志服务接口
│     ├─ service\SysMailService.java                                   # 邮件服务接口
│     ├─ service\FeedbackService.java                                  # 反馈服务接口
│     ├─ service\impl\SysOperLogServiceImpl.java                       # 操作日志服务实现
│     ├─ service\impl\FeedbackServiceImpl.java                         # 反馈服务实现
│     ├─ service\impl\SysMailServiceImpl.java                          # 邮件服务实现
│     ├─ excpetion\SysErrorCode.java                                   # 系统错误码
│     ├─ mapper\SysOperLogMapper.java                                  # 系统日志 Mapper
│     ├─ mapper\FeedbackMapper.java                                    # 反馈 Mapper
│     ├─ controller\RemoteLogController.java                           # 远程日志控制器
│     ├─ controller\UserFeedbackController.java                        # 用户反馈控制器
│     ├─ controller\MailController.java                                # 邮件控制器
│     ├─ domain\entity\SysOperLogEntity.java                           # 系统操作日志实体
│     └─ domain\entity\FeedbackEntity.java                             # 反馈实体
├─ wisepen-fudan-extension-service
│  ├─ wisepen-fudan-extension-api\src\main\java\com\oriole\wisepen\extension\fudan
│  │  ├─ feign\RemoteFudanExtensionService.java                        # 复旦扩展 Feign 接口
│  │  ├─ enums\FudanUISTaskState.java                                  # UIS 任务状态枚举
│  │  ├─ constant\MqTopicConstants.java                                # 扩展域 MQ Topic 常量
│  │  ├─ domain\mq\FudanUISAuthRequestMessage.java                     # UIS 鉴权消息体
│  │  └─ domain\dto\FudanUISTaskResultDTO.java                         # UIS 任务结果 DTO
│  └─ wisepen-fudan-extension-biz\src\main\java\com\oriole\wisepen\extension\fudan
│     ├─ FudanExtensionApplication.java                                # 复旦扩展服务启动入口
│     ├─ exception\UISErrorCode.java                                   # UIS 错误码
│     ├─ cache\RedisCacheManager.java                                  # 扩展缓存管理
│     ├─ controller\UISTaskController.java                             # UIS 任务控制器
│     └─ consumer\UISAuthRequestConsumer.java                          # UIS 鉴权请求消费者
└─ wisepen-user-service
   ├─ wisepen-user-api\src\main\java\com\oriole\wisepen\user\api
   │  ├─ validation\ValidUsernameValidator.java                        # 用户名校验器
   │  ├─ validation\ValidUsername.java                                 # 用户名校验注解
   │  ├─ feign\RemoteUserService.java                                  # 用户 Feign 对外接口
   │  ├─ constant\UserValidationMsg.java                               # 用户校验文案常量
   │  ├─ constant\UserRegexPatterns.java                               # 用户正则常量
   │  ├─ constant\MqTopicConstants.java                                # 用户域 MQ Topic 常量
   │  ├─ constant\GroupValidationMsg.java                              # 小组校验文案常量
   │  ├─ config\UserProperties.java                                    # 用户模块配置
   │  ├─ enums\VoucherStatus.java                                      # 券状态枚举
   │  ├─ enums\UserVerificationMode.java                               # 用户验证模式枚举
   │  ├─ enums\TokenTransferType.java                                  # 代币转移类型枚举
   │  ├─ enums\TokenTransactionType.java                               # 代币交易类型枚举
   │  ├─ enums\TokenPayerType.java                                     # 代币支付方类型枚举
   │  ├─ enums\Status.java                                             # 通用状态枚举
   │  ├─ enums\ModelType.java                                          # 模型类型枚举
   │  ├─ enums\GroupRoleFilter.java                                    # 小组角色筛选枚举
   │  ├─ enums\GenderType.java                                         # 性别枚举
   │  ├─ enums\DegreeLevel.java                                        # 学位层级枚举
   │  ├─ domain\mq\TokenConsumptionMessage.java                        # 代币消费消息体
   │  ├─ domain\base\UserProfileBase.java                              # 用户资料基础模型
   │  ├─ domain\base\UserInfoBase.java                                 # 用户信息基础模型
   │  ├─ domain\base\UserDisplayBase.java                              # 用户展示基础模型
   │  ├─ domain\base\TokenTransactionRecordBase.java                   # 交易基础模型
   │  ├─ domain\base\GroupMemberBase.java                              # 成员基础模型
   │  ├─ domain\base\GroupInfoBase.java                                # 小组信息基础模型
   │  ├─ domain\base\GroupIdentityBase.java                            # 小组身份基础模型
   │  ├─ domain\base\GroupDisplayBase.java                             # 小组展示基础模型
   │  ├─ domain\dto\VerificationResultDTO.java                         # 验证结果 DTO
   │  ├─ domain\dto\res\WalletTransactionRecordResponse.java           # 钱包交易响应 DTO
   │  ├─ domain\dto\res\WalletDetailResponse.java                      # 钱包详情响应 DTO
   │  ├─ domain\dto\res\UserDetailInfoResponse.java                    # 用户详情响应 DTO
   │  ├─ domain\dto\res\GroupMemberTokenDetailResponse.java            # 成员代币详情响应 DTO
   │  ├─ domain\dto\res\GroupMemberDetailResponse.java                 # 成员详情响应 DTO
   │  ├─ domain\dto\res\GroupItemInfoResponse.java                     # 小组条目响应 DTO
   │  ├─ domain\dto\res\GroupDetailInfoResponse.java                   # 小组详情响应 DTO
   │  ├─ domain\dto\req\AuthPwdAdminResetRequest.java                  # 管理员重置密码请求 DTO
   │  ├─ domain\dto\req\AuthLoginRequest.java                          # 登录请求 DTO
   │  ├─ domain\dto\req\AuthPwdResetRequest.java                       # 重置密码请求 DTO
   │  ├─ domain\dto\req\AuthPwdResetVerifyRequest.java                 # 重置验证请求 DTO
   │  ├─ domain\dto\req\GroupMemberKickRequest.java                    # 踢出成员请求 DTO
   │  ├─ domain\dto\req\GroupCreateRequest.java                        # 创建小组请求 DTO
   │  ├─ domain\dto\req\GroupMemberJoinRequest.java                    # 加入小组请求 DTO
   │  ├─ domain\dto\req\AuthRegisterRequest.java                       # 注册请求 DTO
   │  ├─ domain\dto\req\GroupDeleteRequest.java                        # 删除小组请求 DTO
   │  ├─ domain\dto\req\GroupMemberTokenLimitUpdateRequest.java        # 成员代币限额更新请求 DTO
   │  ├─ domain\dto\req\GroupMemberRoleUpdateRequest.java              # 成员角色更新请求 DTO
   │  ├─ domain\dto\req\UserInfoAdminUpdateRequest.java                # 管理员用户信息更新请求 DTO
   │  ├─ domain\dto\req\GroupMemberQuitRequest.java                    # 退出小组请求 DTO
   │  ├─ domain\dto\req\GroupUpdateRequest.java                        # 小组更新请求 DTO
   │  ├─ domain\dto\req\UserInfoUpdateRequest.java                     # 用户信息更新请求 DTO
   │  ├─ domain\dto\req\WalletTransferTokenRequest.java                # 代币转账请求 DTO
   │  ├─ domain\dto\req\WalletRedeemVoucherRequest.java                # 券兑换请求 DTO
   │  ├─ domain\dto\req\UserProfileUpdateRequest.java                  # 用户资料更新请求 DTO
   │  └─ domain\dto\req\UserProfileAdminUpdateRequest.java             # 管理员资料更新请求 DTO
   └─ wisepen-user-biz\src\main\java\com\oriole\wisepen\user
      ├─ UserApplication.java                                          # 用户服务启动入口
      ├─ strategy\VerificationStrategyFactory.java                     # 验证策略工厂
      ├─ strategy\UserVerificationStrategy.java                        # 用户验证策略接口
      ├─ strategy\impl\FudanUISVerificationStrategy.java               # UIS 验证策略实现
      ├─ strategy\impl\EmailVerificationStrategy.java                  # 邮箱验证策略实现
      ├─ service\IWalletService.java                                   # 钱包服务接口
      ├─ service\IUserService.java                                     # 用户服务接口
      ├─ exception\UserErrorCode.java                                  # 用户错误码
      ├─ exception\GroupErrorCode.java                                 # 小组错误码
      ├─ service\IGroupMemberService.java                              # 小组成员服务接口
      ├─ service\AuthService.java                                      # 认证服务
      ├─ service\IGroupService.java                                    # 小组服务接口
      ├─ event\GroupTokenConsumeEvent.java                             # 小组代币消费事件
      ├─ config\MybatisPlusConfig.java                                 # MyBatis 配置
      ├─ service\impl\WalletServiceImpl.java                           # 钱包服务实现
      ├─ service\impl\UserServiceImpl.java                             # 用户服务实现
      ├─ service\impl\GroupServiceImpl.java                            # 小组服务实现
      ├─ service\impl\GroupMemberServiceImpl.java                      # 小组成员服务实现
      ├─ cache\RedisCacheManager.java                                  # 用户域缓存管理
      ├─ controller\WalletController.java                              # 钱包控制器
      ├─ controller\UserController.java                                # 用户控制器
      ├─ consumer\TokenConsumptionConsumer.java                        # 代币消费消费者
      ├─ controller\InternalController.java                            # 内部接口控制器
      ├─ controller\GroupMemberController.java                         # 小组成员控制器
      ├─ controller\AuthController.java                                # 认证控制器
      ├─ controller\GroupController.java                               # 小组控制器
      ├─ controller\AdminUserController.java                           # 管理员用户控制器
      ├─ mapper\TokenVoucherMapper.java                                # 兑换券 Mapper
      ├─ mapper\TokenTransactionRecordMapper.java                      # 交易记录 Mapper
      ├─ mapper\GroupMemberMapper.java                                 # 小组成员 Mapper
      ├─ mapper\GroupMapper.java                                       # 小组 Mapper
      ├─ mapper\UserMapper.java                                        # 用户 Mapper
      ├─ mapper\UserProfileMapper.java                                 # 用户资料 Mapper
      ├─ mapper\UserWalletsMapper.java                                 # 用户钱包 Mapper
      ├─ domain\entity\GroupEntity.java                                # 小组实体
      ├─ domain\entity\GroupMemberEntity.java                          # 小组成员实体
      ├─ domain\entity\TokenTransactionRecordEntity.java               # 交易记录实体
      ├─ domain\entity\TokenVoucherEntity.java                         # 兑换券实体
      ├─ domain\entity\UserWalletEntity.java                           # 用户钱包实体
      ├─ domain\entity\UserProfileEntity.java                          # 用户资料实体
      └─ domain\entity\UserEntity.java                                 # 用户实体
```

