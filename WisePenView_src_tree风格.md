# WisePenView src 全量文件清单（tree 风格）

> 说明：已按参考文档 tree 风格整理 `WisePenView\src`；文本文件通过批量全文读取（rg 全量抓取），二进制文件读取头部/元数据（图片通过 view 读取）。

```text
WisePenView\src
├─ WisePenView\src\vite-env.d.ts    # TypeScript 环境声明文件
├─ WisePenView\src\App.tsx    # 前端应用根组件
├─ WisePenView\src\App.module.less    # 应用根组件样式
├─ WisePenView\src\main.tsx    # 前端应用入口初始化
├─ WisePenView\src\router.tsx    # 前端路由配置
├─ WisePenView\src\index.css    # 全局样式定义
├─ WisePenView\src\layouts\SystemLayout.tsx    # 系统页面布局组件
├─ WisePenView\src\layouts\SystemLayout.module.less    # 系统布局样式
├─ WisePenView\src\layouts\HomeLayout.tsx    # 首页布局组件
├─ WisePenView\src\layouts\HomeLayout.module.less    # 首页布局样式
├─ WisePenView\src\layouts\AuthLayout.tsx    # 认证页面布局组件
├─ WisePenView\src\layouts\AuthLayout.module.less    # 认证布局样式
├─ WisePenView\src\views\profile\Usage\index.tsx    # 个人中心用量页面组件
├─ WisePenView\src\views\profile\style.module.less    # 个人中心页面样式
├─ WisePenView\src\views\profile\profile.config.ts    # 个人中心配置定义
├─ WisePenView\src\theme\index.ts    # 主题配置入口
├─ WisePenView\src\views\error\ResourceNotFound\style.module.less    # 资源未找到页样式
├─ WisePenView\src\views\error\ResourceNotFound\index.tsx    # 资源未找到错误页面
├─ WisePenView\src\views\profile\Account\index.tsx    # 账号信息页面组件
├─ WisePenView\src\hooks\useSmoothFlag.ts    # 自定义状态平滑 Hook
├─ WisePenView\src\hooks\useParamsEffect.ts    # 参数变更副作用 Hook
├─ WisePenView\src\hooks\useEffectForce.ts    # 强制触发副作用 Hook
├─ WisePenView\src\hooks\useClickFile.ts    # 文件点击交互 Hook
├─ WisePenView\src\hooks\useAppMessage.ts    # 应用消息提示 Hook
├─ WisePenView\src\views\error\AppError\style.module.less    # 通用错误页样式
├─ WisePenView\src\views\error\AppError\index.tsx    # 通用错误页面组件
├─ WisePenView\src\session\chat\useChatSession.ts    # 聊天会话管理 Hook
├─ WisePenView\src\session\chat\index.type.ts    # 聊天会话类型定义
├─ WisePenView\src\services\Image\index.type.ts    # 图片服务类型定义
├─ WisePenView\src\services\Image\index.ts    # 图片服务导出入口
├─ WisePenView\src\services\Image\ImageServices.impl.ts    # 图片服务实现
├─ WisePenView\src\components\Wallet\RechargeModal\style.module.less    # 充值弹窗样式
├─ WisePenView\src\components\Wallet\RechargeModal\index.type.ts    # 充值弹窗类型定义
├─ WisePenView\src\components\Wallet\RechargeModal\index.tsx    # 充值弹窗组件
├─ WisePenView\src\session\note\WisepenProvider.ts    # 笔记会话 Provider
├─ WisePenView\src\session\note\useNoteSession.ts    # 笔记会话管理 Hook
├─ WisePenView\src\session\note\NoteStatusObserver.ts    # 笔记状态观察器
├─ WisePenView\src\assets\images\logo.png    # PNG 品牌 Logo 资源
├─ WisePenView\src\assets\images\logo-white.png    # PNG 白色 Logo 资源
├─ WisePenView\src\assets\images\logo-icon.png    # PNG 图标 Logo 资源
├─ WisePenView\src\assets\images\login.png    # PNG 登录页插画资源
├─ WisePenView\src\services\Wallet\WalletServices.impl.ts    # 钱包服务实现
├─ WisePenView\src\services\Wallet\index.type.ts    # 钱包服务类型定义
├─ WisePenView\src\services\Wallet\index.ts    # 钱包服务导出入口
├─ WisePenView\src\services\Chat\model.mapper.ts    # 聊天模型映射逻辑
├─ WisePenView\src\services\Chat\index.type.ts    # 聊天服务类型定义
├─ WisePenView\src\services\Chat\index.ts    # 聊天服务导出入口
├─ WisePenView\src\services\Chat\ChatServices.impl.ts    # 聊天服务实现
├─ WisePenView\src\services\cacheRegistry.ts    # 服务缓存注册与清理
├─ WisePenView\src\hooks\drive\useTreeDriveDrop.ts    # 目录树拖拽 Hook
├─ WisePenView\src\hooks\drive\useTreeDrive.type.ts    # 目录树 Hook 类型定义
├─ WisePenView\src\hooks\drive\useTreeDrive.ts    # 目录树数据 Hook
├─ WisePenView\src\hooks\drive\treeRowDataUtil.ts    # 目录树行数据工具
├─ WisePenView\src\contexts\ServicesContext\ServicesProvider.tsx    # 服务上下文 Provider
├─ WisePenView\src\hooks\drive\index.ts    # 目录树 Hook 导出
├─ WisePenView\src\contexts\ServicesContext\registry.types.ts    # 服务注册类型定义
├─ WisePenView\src\contexts\ServicesContext\registry.ts    # 服务注册入口
├─ WisePenView\src\contexts\ServicesContext\registry.mock.ts    # 服务注册 Mock 实现
├─ WisePenView\src\contexts\ServicesContext\registry.impl.ts    # 服务注册真实实现
├─ WisePenView\src\contexts\ServicesContext\index.ts    # 服务上下文导出
├─ WisePenView\src\contexts\ServicesContext\hooks.ts    # 服务上下文 Hook
├─ WisePenView\src\contexts\ServicesContext\context.ts    # 服务上下文对象定义
├─ WisePenView\src\mocks\Wallet\WalletServices.mock.ts    # 钱包服务 Mock 实现
├─ WisePenView\src\mocks\Wallet\mockdata.json    # 钱包 Mock 数据
├─ WisePenView\src\store\zustand\useTreeDriveCwdStore.ts    # 目录当前路径状态仓库
├─ WisePenView\src\store\zustand\useTrashTagStore.ts    # 回收站标签状态仓库
├─ WisePenView\src\store\zustand\useRecentFilesStore.ts    # 最近文件状态仓库
├─ WisePenView\src\store\zustand\usePdfPreviewProgressStore.ts    # PDF 预览进度状态仓库
├─ WisePenView\src\store\zustand\useNoteSelectionStore.ts    # 笔记选中状态仓库
├─ WisePenView\src\store\zustand\useNewNoteStore.ts    # 新建笔记状态仓库
├─ WisePenView\src\store\zustand\useNewChatSessionStore.ts    # 新建聊天会话状态仓库
├─ WisePenView\src\store\zustand\useDrivePreferencesStore.ts    # 文件区偏好状态仓库
├─ WisePenView\src\store\zustand\useCurrentChatSessionStore.ts    # 当前聊天会话状态仓库
├─ WisePenView\src\store\zustand\useChatPanelStore.ts    # 聊天面板状态仓库
├─ WisePenView\src\store\zustand\useChatModelPreferenceStore.ts    # 聊天模型偏好状态仓库
├─ WisePenView\src\store\zustand\sessionStorage.ts    # 会话存储适配器
├─ WisePenView\src\store\zustand\index.ts    # zustand 仓库导出
├─ WisePenView\src\store\zustand\clearAllStores.ts    # 全量状态清理工具
├─ WisePenView\src\store\index.ts    # 状态仓库总入口
├─ WisePenView\src\views\pdf\PdfPreview\style.module.less    # PDF 预览页样式
├─ WisePenView\src\views\pdf\PdfPreview\index.tsx    # PDF 预览页面组件
├─ WisePenView\src\services\Group\index.type.ts    # 组织服务类型定义
├─ WisePenView\src\services\Group\index.ts    # 组织服务导出入口
├─ WisePenView\src\services\Group\GroupServices.impl.ts    # 组织服务实现
├─ WisePenView\src\services\Group\groupMember.mapper.ts    # 组织成员映射工具
├─ WisePenView\src\components\Wallet\ComputeWallet\style.module.less    # 算力钱包组件样式
├─ WisePenView\src\components\Wallet\ComputeWallet\index.type.ts    # 算力钱包组件类型定义
├─ WisePenView\src\components\Wallet\ComputeWallet\index.tsx    # 算力钱包组件
├─ WisePenView\src\services\User\UserServices.impl.ts    # 用户服务实现
├─ WisePenView\src\services\User\index.type.ts    # 用户服务类型定义
├─ WisePenView\src\services\User\index.ts    # 用户服务导出入口
├─ WisePenView\src\constants\wallet.ts    # 钱包常量定义
├─ WisePenView\src\constants\user.ts    # 用户常量定义
├─ WisePenView\src\constants\resource.ts    # 资源常量定义
├─ WisePenView\src\constants\quota.ts    # 配额常量定义
├─ WisePenView\src\constants\group.ts    # 组织常量定义
├─ WisePenView\src\constants\document.ts    # 文档常量定义
├─ WisePenView\src\assets\images\empty\noRecord.svg    # SVG 空态图资源
├─ WisePenView\src\services\Auth\index.type.ts    # 认证服务类型定义
├─ WisePenView\src\services\Auth\index.ts    # 认证服务导出入口
├─ WisePenView\src\services\Auth\AuthServices.impl.ts    # 认证服务实现
├─ WisePenView\src\services\Folder\index.type.ts    # 文件夹服务类型定义
├─ WisePenView\src\services\Folder\index.ts    # 文件夹服务导出入口
├─ WisePenView\src\services\Folder\FolderServices.impl.ts    # 文件夹服务实现
├─ WisePenView\src\components\UserProfile\style.module.less    # 用户资料组件样式
├─ WisePenView\src\components\UserProfile\index.tsx    # 用户资料组件
├─ WisePenView\src\views\note\style.module.less    # 笔记页样式
├─ WisePenView\src\views\note\index.tsx    # 笔记页面组件
├─ WisePenView\src\views\drive\Drive\UploadDocumentModal.tsx    # 上传文档弹窗组件
├─ WisePenView\src\views\drive\Drive\UploadDocumentModal.module.less    # 上传文档弹窗样式
├─ WisePenView\src\views\drive\Drive\style.module.less    # 文件区页面样式
├─ WisePenView\src\views\drive\Drive\index.tsx    # 文件区页面组件
├─ WisePenView\src\mocks\User\UserServices.mock.ts    # 用户服务 Mock 实现
├─ WisePenView\src\mocks\User\mockdata.json    # 用户 Mock 数据
├─ WisePenView\src\assets\images\backgrounds\search.svg    # SVG 搜索背景资源
├─ WisePenView\src\assets\images\backgrounds\relation.svg    # SVG 关系背景资源
├─ WisePenView\src\assets\images\backgrounds\polyline-edit.svg    # SVG 折线编辑背景资源
├─ WisePenView\src\assets\images\backgrounds\node.svg    # SVG 节点背景资源
├─ WisePenView\src\services\Document\index.type.ts    # 文档服务类型定义
├─ WisePenView\src\services\Document\index.ts    # 文档服务导出入口
├─ WisePenView\src\services\Document\DocumentServices.impl.ts    # 文档服务实现
├─ WisePenView\src\services\Quota\QuotaServices.impl.ts    # 配额服务实现
├─ WisePenView\src\services\Quota\index.type.ts    # 配额服务类型定义
├─ WisePenView\src\services\Quota\index.ts    # 配额服务导出入口
├─ WisePenView\src\types\wallet.ts    # 钱包领域类型定义
├─ WisePenView\src\components\Sidebar\style.module.less    # 侧边栏组件样式
├─ WisePenView\src\types\user.ts    # 用户领域类型定义
├─ WisePenView\src\types\tag.ts    # 标签领域类型定义
├─ WisePenView\src\types\resource.ts    # 资源领域类型定义
├─ WisePenView\src\types\quota.ts    # 配额领域类型定义
├─ WisePenView\src\types\note.ts    # 笔记领域类型定义
├─ WisePenView\src\types\model.ts    # 模型领域类型定义
├─ WisePenView\src\types\group.ts    # 组织领域类型定义
├─ WisePenView\src\types\folder.ts    # 文件夹领域类型定义
├─ WisePenView\src\types\api.ts    # API 通用类型定义
├─ WisePenView\src\utils\number.ts    # 数字处理工具
├─ WisePenView\src\utils\normalizeTagGroupId.ts    # 标签组 ID 标准化工具
├─ WisePenView\src\utils\image.ts    # 图片处理工具
├─ WisePenView\src\utils\format.ts    # 格式化工具集合
├─ WisePenView\src\utils\computeFileMd5.ts    # 文件 MD5 计算工具
├─ WisePenView\src\utils\Axios.ts    # Axios 客户端封装
├─ WisePenView\src\utils\authSync.ts    # 认证状态同步工具
├─ WisePenView\src\utils\apiServerAddr.ts    # API 服务地址解析工具
├─ WisePenView\src\utils\path.ts    # 路径处理工具
├─ WisePenView\src\utils\parseErrorMessage.ts    # 错误信息解析工具
├─ WisePenView\src\utils\ossPresignedPut.ts    # OSS 预签名上传工具
├─ WisePenView\src\utils\openedResourceRoute.ts    # 资源打开路由工具
├─ WisePenView\src\utils\serializeRepeatKeyQuery.ts    # 重复键查询串序列化工具
├─ WisePenView\src\utils\response.ts    # 响应数据处理工具
├─ WisePenView\src\utils\time.ts    # 时间处理工具
├─ WisePenView\src\utils\validateReservedName.ts    # 保留名校验工具
├─ WisePenView\src\views\home\style.module.less    # 首页样式
├─ WisePenView\src\views\home\index.tsx    # 首页组件
├─ WisePenView\src\components\Sidebar\index.type.ts    # 侧边栏类型定义
├─ WisePenView\src\components\Sidebar\index.tsx    # 侧边栏组件
├─ WisePenView\src\services\Tag\TagServices.impl.ts    # 标签服务实现
├─ WisePenView\src\services\Tag\index.type.ts    # 标签服务类型定义
├─ WisePenView\src\services\Tag\index.ts    # 标签服务导出入口
├─ WisePenView\src\components\LandingNavbar\style.module.less    # 落地页导航样式
├─ WisePenView\src\components\LandingNavbar\index.type.ts    # 落地页导航类型定义
├─ WisePenView\src\components\LandingNavbar\index.tsx    # 落地页导航组件
├─ WisePenView\src\services\Note\NoteServices.impl.ts    # 笔记服务实现
├─ WisePenView\src\services\Note\index.type.ts    # 笔记服务类型定义
├─ WisePenView\src\services\Note\index.ts    # 笔记服务导出入口
├─ WisePenView\src\services\Resource\ResourceServices.impl.ts    # 资源服务实现
├─ WisePenView\src\services\Resource\index.type.ts    # 资源服务类型定义
├─ WisePenView\src\services\Resource\index.ts    # 资源服务导出入口
├─ WisePenView\src\views\group\style.module.less    # 组织页样式
├─ WisePenView\src\components\Sidebar\SidebarMenu\style.module.less    # 侧边栏菜单样式
├─ WisePenView\src\components\Sidebar\SidebarMenu\index.type.ts    # 侧边栏菜单类型定义
├─ WisePenView\src\components\Sidebar\SidebarMenu\index.tsx    # 侧边栏菜单组件
├─ WisePenView\src\mocks\Tag\TagServices.mock.ts    # 标签服务 Mock 实现
├─ WisePenView\src\mocks\Tag\mockdata.json    # 标签 Mock 数据
├─ WisePenView\src\components\Pdf\PdfViewer\style.module.less    # PDF 查看器样式
├─ WisePenView\src\components\Pdf\PdfViewer\pdf.config.ts    # PDF 查看器配置
├─ WisePenView\src\components\Pdf\PdfViewer\index.type.ts    # PDF 查看器类型定义
├─ WisePenView\src\components\Pdf\PdfViewer\index.tsx    # PDF 查看器组件
├─ WisePenView\src\mocks\Group\mockdata.json    # 组织 Mock 数据
├─ WisePenView\src\mocks\Group\GroupServices.mock.ts    # 组织服务 Mock 实现
├─ WisePenView\src\components\Sidebar\HeaderNav\style.module.less    # 侧边栏头部导航样式
├─ WisePenView\src\components\Sidebar\HeaderNav\index.type.ts    # 侧边栏头部导航类型定义
├─ WisePenView\src\components\Sidebar\HeaderNav\index.tsx    # 侧边栏头部导航组件
├─ WisePenView\src\views\group\MyGroup\style.module.less    # 我的组织页样式
├─ WisePenView\src\views\group\MyGroup\index.tsx    # 我的组织页面组件
├─ WisePenView\src\components\Sidebar\SidebarHeader\style.module.less    # 侧边栏头部样式
├─ WisePenView\src\views\auth\VerifyEmail\index.tsx    # 邮箱验证页面组件
├─ WisePenView\src\components\Sidebar\SidebarHeader\index.type.ts    # 侧边栏头部类型定义
├─ WisePenView\src\components\Sidebar\SidebarHeader\index.tsx    # 侧边栏头部组件
├─ WisePenView\src\mocks\Quota\QuotaServices.mock.ts    # 配额服务 Mock 实现
├─ WisePenView\src\mocks\Quota\mockdata.json    # 配额 Mock 数据
├─ WisePenView\src\services\Sticker\StickerServices.impl.ts    # 贴纸服务实现
├─ WisePenView\src\services\Sticker\index.type.ts    # 贴纸服务类型定义
├─ WisePenView\src\services\Sticker\index.ts    # 贴纸服务导出入口
├─ WisePenView\src\mocks\Folder\mockdata.json    # 文件夹 Mock 数据
├─ WisePenView\src\mocks\Folder\FolderServices.mock.ts    # 文件夹服务 Mock 实现
├─ WisePenView\src\views\group\GroupDetail\style.module.less    # 组织详情页样式
├─ WisePenView\src\views\group\GroupDetail\index.tsx    # 组织详情页面组件
├─ WisePenView\src\components\Sidebar\SessionSection\style.module.less    # 会话分区样式
├─ WisePenView\src\components\Sidebar\SessionSection\index.type.ts    # 会话分区类型定义
├─ WisePenView\src\components\Sidebar\SessionSection\index.tsx    # 会话分区组件
├─ WisePenView\src\mocks\Note\NoteServices.mock.ts    # 笔记服务 Mock 实现
├─ WisePenView\src\components\ServiceAgreement\style.module.less    # 服务协议组件样式
├─ WisePenView\src\components\ServiceAgreement\index.type.tsx    # 服务协议组件类型定义
├─ WisePenView\src\components\ServiceAgreement\index.tsx    # 服务协议组件
├─ WisePenView\src\components\Common\UserCapsule\style.module.less    # 用户胶囊组件样式
├─ WisePenView\src\components\Common\UserCapsule\index.type.ts    # 用户胶囊组件类型定义
├─ WisePenView\src\components\Common\UserCapsule\index.tsx    # 用户胶囊组件
├─ WisePenView\src\mocks\Sticker\StickerServices.mock.ts    # 贴纸服务 Mock 实现
├─ WisePenView\src\components\Sidebar\SessionListGroup\style.module.less    # 会话列表组样式
├─ WisePenView\src\components\Sidebar\SessionListGroup\index.type.ts    # 会话列表组类型定义
├─ WisePenView\src\components\Sidebar\SessionListGroup\index.tsx    # 会话列表组组件
├─ WisePenView\src\views\auth\ResetPassword\index.tsx    # 重置密码页面组件
├─ WisePenView\src\mocks\Document\DocumentServices.mock.ts    # 文档服务 Mock 实现
├─ WisePenView\src\components\Note\NoteTitle\style.module.less    # 笔记标题组件样式
├─ WisePenView\src\components\Note\NoteTitle\index.type.ts    # 笔记标题组件类型定义
├─ WisePenView\src\components\Note\NoteTitle\index.tsx    # 笔记标题组件
├─ WisePenView\src\mocks\Resource\ResourceServices.mock.ts    # 资源服务 Mock 实现
├─ WisePenView\src\mocks\Resource\mockdata.json    # 资源 Mock 数据
├─ WisePenView\src\mocks\Image\ImageServices.mock.ts    # 图片服务 Mock 实现
├─ WisePenView\src\views\auth\Auth.module.less    # 认证页面公共样式
├─ WisePenView\src\mocks\Auth\mockdata.json    # 认证 Mock 数据
├─ WisePenView\src\mocks\Auth\AuthServices.mock.ts    # 认证服务 Mock 实现
├─ WisePenView\src\components\Group\OwnerGroupTokenTransfer\style.module.less    # 组织 Token 转移组件样式
├─ WisePenView\src\components\Group\OwnerGroupTokenTransfer\index.type.ts    # 组织 Token 转移组件类型定义
├─ WisePenView\src\components\Group\OwnerGroupTokenTransfer\index.tsx    # 组织 Token 转移组件
├─ WisePenView\src\components\Sidebar\SessionMenuItem\style.module.less    # 会话菜单项样式
├─ WisePenView\src\components\Sidebar\SessionMenuItem\index.type.ts    # 会话菜单项类型定义
├─ WisePenView\src\components\Sidebar\SessionMenuItem\index.tsx    # 会话菜单项组件
├─ WisePenView\src\mocks\Chat\ChatServices.mock.ts    # 聊天服务 Mock 实现
├─ WisePenView\src\components\Profile\QuotaByGroup\style.module.less    # 组织配额组件样式
├─ WisePenView\src\components\Profile\QuotaByGroup\index.type.tsx    # 组织配额组件类型定义
├─ WisePenView\src\components\Profile\QuotaByGroup\index.tsx    # 组织配额组件
├─ WisePenView\src\views\auth\Login\index.tsx    # 登录页面组件
├─ WisePenView\src\components\Common\SelectedMemberList\index.type.tsx    # 已选成员列表类型定义
├─ WisePenView\src\components\Common\SelectedMemberList\index.tsx    # 已选成员列表组件
├─ WisePenView\src\components\Common\SelectedMemberList\style.module.less    # 已选成员列表样式
├─ WisePenView\src\components\Group\GroupDisplayConfig.ts    # 组织展示配置
├─ WisePenView\src\components\Sidebar\RecentFilesGroup\style.module.less    # 最近文件分组样式
├─ WisePenView\src\components\Sidebar\RecentFilesGroup\index.type.ts    # 最近文件分组类型定义
├─ WisePenView\src\components\Sidebar\RecentFilesGroup\index.tsx    # 最近文件分组组件
├─ WisePenView\src\components\Common\FileTypeIcon\index.type.ts    # 文件类型图标类型定义
├─ WisePenView\src\components\Common\FileTypeIcon\index.tsx    # 文件类型图标组件
├─ WisePenView\src\components\ChatPanel\style.module.less    # 聊天面板样式
├─ WisePenView\src\views\auth\Register\index.tsx    # 注册页面组件
├─ WisePenView\src\components\Note\NoteInfoBar\style.module.less    # 笔记信息栏样式
├─ WisePenView\src\components\Note\NoteInfoBar\index.type.ts    # 笔记信息栏类型定义
├─ WisePenView\src\components\Note\NoteInfoBar\index.tsx    # 笔记信息栏组件
├─ WisePenView\src\views\auth\NewPassword\index.tsx    # 新密码设置页面组件
├─ WisePenView\src\components\Common\QuotaBar\style.module.less    # 配额条组件样式
├─ WisePenView\src\components\Common\QuotaBar\index.type.tsx    # 配额条组件类型定义
├─ WisePenView\src\components\Common\QuotaBar\index.tsx    # 配额条组件
├─ WisePenView\src\components\ChatPanel\index.tsx    # 聊天面板入口组件
├─ WisePenView\src\components\ChatPanel\ChatPanel.ts    # 聊天面板核心逻辑
├─ WisePenView\src\components\ChatPanel\index.type.ts    # 聊天面板类型定义
├─ WisePenView\src\components\Group\MemberList\style.module.less    # 成员列表样式
├─ WisePenView\src\components\Account\VerifyBanner\style.module.less    # 验证提示条样式
├─ WisePenView\src\components\Account\VerifyBanner\index.type.ts    # 验证提示条类型定义
├─ WisePenView\src\components\Account\VerifyBanner\index.tsx    # 验证提示条组件
├─ WisePenView\src\components\Account\index.ts    # 账号组件导出入口
├─ WisePenView\src\components\ChatPanel\ModelSelector\style.module.less    # 模型选择器样式
├─ WisePenView\src\components\ChatPanel\ModelSelector\index.tsx    # 模型选择器组件
├─ WisePenView\src\components\Group\GroupCard\style.module.less    # 组织卡片样式
├─ WisePenView\src\components\Group\GroupCard\index.type.tsx    # 组织卡片类型定义
├─ WisePenView\src\components\Group\GroupCard\index.tsx    # 组织卡片组件
├─ WisePenView\src\components\Group\MemberList\index.type.tsx    # 成员列表类型定义
├─ WisePenView\src\components\Group\MemberList\index.tsx    # 成员列表组件
├─ WisePenView\src\components\Account\AccountHeader\style.module.less    # 账号头部样式
├─ WisePenView\src\components\Account\AccountHeader\index.type.ts    # 账号头部类型定义
├─ WisePenView\src\components\Common\ReadOnlyBreadcrumb\style.module.less    # 只读面包屑样式
├─ WisePenView\src\components\Account\AccountHeader\index.tsx    # 账号头部组件
├─ WisePenView\src\components\Common\ReadOnlyBreadcrumb\index.type.ts    # 只读面包屑类型定义
├─ WisePenView\src\components\Common\ReadOnlyBreadcrumb\index.tsx    # 只读面包屑组件
├─ WisePenView\src\components\ChatPanel\ChatInput\style.module.less    # 聊天输入区样式
├─ WisePenView\src\components\ChatPanel\ChatInput\index.tsx    # 聊天输入区组件
├─ WisePenView\src\components\ChatPanel\ChatInput\ActionToolbar.tsx    # 聊天输入操作栏
├─ WisePenView\src\components\Group\MemberList\Modals\useMemberEditGuard.ts    # 成员编辑守卫 Hook
├─ WisePenView\src\components\Group\MemberList\Modals\style.module.less    # 成员弹窗样式
├─ WisePenView\src\components\Group\MemberList\Modals\InviteUserModal.tsx    # 邀请成员弹窗组件
├─ WisePenView\src\components\Group\MemberList\Modals\index.type.tsx    # 成员弹窗类型定义
├─ WisePenView\src\components\Group\MemberList\Modals\index.ts    # 成员弹窗导出入口
├─ WisePenView\src\components\Group\MemberList\Modals\EditPermissionModal.tsx    # 编辑权限弹窗组件
├─ WisePenView\src\components\Group\MemberList\Modals\DeleteMemberModal.tsx    # 删除成员弹窗组件
├─ WisePenView\src\components\Group\MemberList\Modals\AssignQuotaModal.tsx    # 分配配额弹窗组件
├─ WisePenView\src\components\Note\CustomBlockNote\useNoteYjsUndoStack.ts    # 笔记撤销栈 Hook
├─ WisePenView\src\components\Note\CustomBlockNote\useNoteCaptureKeyEvent.ts    # 笔记按键捕获 Hook
├─ WisePenView\src\components\Note\CustomBlockNote\style.module.less    # 自定义笔记编辑器样式
├─ WisePenView\src\components\Note\CustomBlockNote\stripEscapeCharExtension.ts    # 转义字符扩展
├─ WisePenView\src\components\Note\CustomBlockNote\slashMenuConfig.ts    # Slash 菜单配置
├─ WisePenView\src\components\Account\AccountVerification\style.module.less    # 账号验证组件样式
├─ WisePenView\src\components\Account\AccountVerification\resolveUisQrImageDataUrl.ts    # 验证二维码处理工具
├─ WisePenView\src\components\Account\AccountVerification\index.type.ts    # 账号验证组件类型定义
├─ WisePenView\src\components\Account\AccountVerification\index.tsx    # 账号验证组件
├─ WisePenView\src\components\Note\CustomBlockNote\index.tsx    # 自定义笔记编辑器组件
├─ WisePenView\src\components\Note\CustomBlockNote\blockNoteSchema.ts    # 笔记编辑 schema
├─ WisePenView\src\components\Note\CustomBlockNote\index.type.ts    # 自定义笔记编辑器类型定义
├─ WisePenView\src\components\Drive\FlatDrive\style.module.less    # 平铺文件区样式
├─ WisePenView\src\components\Drive\FlatDrive\index.type.ts    # 平铺文件区类型定义
├─ WisePenView\src\components\Drive\FlatDrive\index.tsx    # 平铺文件区组件
├─ WisePenView\src\components\Account\AccountForm\style.module.less    # 账号表单样式
├─ WisePenView\src\components\Account\AccountForm\profileDisplay.ts    # 账号展示配置
├─ WisePenView\src\components\Account\AccountForm\index.type.ts    # 账号表单类型定义
├─ WisePenView\src\components\Account\AccountForm\index.tsx    # 账号表单组件
├─ WisePenView\src\components\Account\AccountForm\buildProfileFormValues.ts    # 表单值构建工具
├─ WisePenView\src\components\Drive\UploadQueueTab\style.module.less    # 上传队列样式
├─ WisePenView\src\components\Drive\UploadQueueTab\index.tsx    # 上传队列组件
├─ WisePenView\src\components\ChatPanel\MessageList\Welcome.tsx    # 聊天欢迎消息组件
├─ WisePenView\src\components\ChatPanel\MessageList\style.module.less    # 聊天消息列表样式
├─ WisePenView\src\components\Group\MemberList\MemberListToolbar\style.module.less    # 成员列表工具栏样式
├─ WisePenView\src\components\Group\MemberList\MemberListToolbar\index.type.tsx    # 成员列表工具栏类型定义
├─ WisePenView\src\components\Group\MemberList\MemberListToolbar\index.tsx    # 成员列表工具栏组件
├─ WisePenView\src\components\Group\GroupModals\JoinGroupModal\index.type.ts    # 加入组织弹窗类型定义
├─ WisePenView\src\components\Group\MemberList\MemberListTable\TableConfig.tsx    # 成员表格配置
├─ WisePenView\src\components\Group\GroupModals\JoinGroupModal\index.tsx    # 加入组织弹窗组件
├─ WisePenView\src\components\Group\GroupModals\JoinGroupModal\index.module.less    # 加入组织弹窗样式
├─ WisePenView\src\components\Group\MemberList\MemberListTable\style.module.less    # 成员表格样式
├─ WisePenView\src\components\Group\MemberList\MemberListTable\index.type.tsx    # 成员表格类型定义
├─ WisePenView\src\components\Group\MemberList\MemberListTable\index.tsx    # 成员表格组件
├─ WisePenView\src\components\Group\GroupModals\index.ts    # 组织弹窗导出入口
├─ WisePenView\src\components\Group\GroupModals\EditGroupInfoModal\index.module.less    # 编辑组织信息弹窗样式
├─ WisePenView\src\components\Group\GroupModals\EditGroupInfoModal\index.tsx    # 编辑组织信息弹窗组件
├─ WisePenView\src\components\Group\GroupModals\EditGroupInfoModal\index.type.ts    # 编辑组织信息弹窗类型定义
├─ WisePenView\src\components\ChatPanel\MessageList\index.tsx    # 聊天消息列表组件
├─ WisePenView\src\components\Drive\FlatDrive\FileList\style.module.less    # 文件列表样式
├─ WisePenView\src\components\Drive\FlatDrive\FileList\index.type.ts    # 文件列表类型定义
├─ WisePenView\src\components\Drive\FlatDrive\FileList\index.tsx    # 文件列表组件
├─ WisePenView\src\components\Group\GroupModals\CreateGroupModal\index.type.ts    # 创建组织弹窗类型定义
├─ WisePenView\src\components\Group\GroupModals\CreateGroupModal\index.tsx    # 创建组织弹窗组件
├─ WisePenView\src\components\Group\GroupModals\CreateGroupModal\index.module.less    # 创建组织弹窗样式
├─ WisePenView\src\components\ChatPanel\MessageList\MessageItem\UserMessage.tsx    # 用户消息组件
├─ WisePenView\src\components\ChatPanel\MessageList\MessageItem\UserMessage.module.less    # 用户消息样式
├─ WisePenView\src\components\ChatPanel\MessageList\MessageItem\ToolCallBlock.tsx    # 工具调用消息块组件
├─ WisePenView\src\components\ChatPanel\MessageList\MessageItem\ToolCallBlock.module.less    # 工具调用消息块样式
├─ WisePenView\src\components\ChatPanel\MessageList\MessageItem\ThinkingBlock.tsx    # 思考过程消息块组件
├─ WisePenView\src\components\ChatPanel\MessageList\MessageItem\ThinkingBlock.module.less    # 思考过程消息块样式
├─ WisePenView\src\components\ChatPanel\MessageList\MessageItem\MessageContent.tsx    # 消息正文渲染组件
├─ WisePenView\src\components\ChatPanel\MessageList\MessageItem\MessageContent.module.less    # 消息正文样式
├─ WisePenView\src\components\ChatPanel\MessageList\MessageItem\index.tsx    # 消息项导出入口
├─ WisePenView\src\components\ChatPanel\MessageList\MessageItem\AiMessage.tsx    # AI 消息组件
├─ WisePenView\src\components\ChatPanel\MessageList\MessageItem\AiMessage.module.less    # AI 消息样式
├─ WisePenView\src\components\Note\CustomBlockNote\LatexSupport\useLatexPopoverAnchorSync.ts    # LaTeX 弹层锚点同步 Hook
├─ WisePenView\src\components\Note\CustomBlockNote\LatexSupport\useFocusPopoverTextarea.ts    # LaTeX 输入聚焦 Hook
├─ WisePenView\src\components\Group\GroupModals\ExitGroupModal\index.type.ts    # 退出组织弹窗类型定义
├─ WisePenView\src\components\Group\GroupModals\ExitGroupModal\index.tsx    # 退出组织弹窗组件
├─ WisePenView\src\components\Group\GroupModals\ExitGroupModal\index.module.less    # 退出组织弹窗样式
├─ WisePenView\src\components\Note\CustomBlockNote\LatexSupport\katexRender.ts    # KaTeX 渲染工具
├─ WisePenView\src\components\Note\CustomBlockNote\LatexSupport\inlineMathDollarExtension.ts    # 行内公式扩展
├─ WisePenView\src\components\Drive\FlatDrive\FileFilter\style.module.less    # 文件筛选器样式
├─ WisePenView\src\components\Drive\FlatDrive\FileFilter\index.type.ts    # 文件筛选器类型定义
├─ WisePenView\src\components\Drive\FlatDrive\FileFilter\index.tsx    # 文件筛选器组件
├─ WisePenView\src\components\Drive\TreeNav\treeNavDataUtil.tsx    # 树导航数据工具
├─ WisePenView\src\components\Drive\TreeNav\tagUtil.tsx    # 树导航标签工具
├─ WisePenView\src\components\Drive\TreeNav\style.module.less    # 树导航样式
├─ WisePenView\src\components\Drive\TreeNav\index.type.ts    # 树导航类型定义
├─ WisePenView\src\components\Drive\TreeNav\index.tsx    # 树导航组件
├─ WisePenView\src\components\Drive\TreeNav\folderUtil.tsx    # 树导航文件夹工具
├─ WisePenView\src\components\Note\CustomBlockNote\LatexSupport\latexPopoverGeometry.ts    # LaTeX 弹层几何计算工具
├─ WisePenView\src\components\Group\GroupModals\DissolveGroupModal\index.tsx    # 解散组织弹窗组件
├─ WisePenView\src\components\Group\GroupModals\DissolveGroupModal\index.module.less    # 解散组织弹窗样式
├─ WisePenView\src\components\Group\GroupModals\DissolveGroupModal\index.type.ts    # 解散组织弹窗类型定义
├─ WisePenView\src\components\Drive\TreeDrive\index.ts    # 树形文件区导出入口
├─ WisePenView\src\components\Drive\TreeDrive\config\rowConfig.ts    # 树形文件区行配置
├─ WisePenView\src\components\Drive\TreeDrive\config\columnConfig.tsx    # 树形文件区列配置
├─ WisePenView\src\components\Drive\TreeDrive\TagDrive\index.tsx    # 标签驱动树组件
├─ WisePenView\src\components\Drive\TreeDrive\style.module.less    # 树形文件区样式
├─ WisePenView\src\components\Drive\TreeDrive\index.type.ts    # 树形文件区类型定义
├─ WisePenView\src\components\Note\CustomBlockNote\LatexSupport\InlineMath\style.module.less    # 行内公式组件样式
├─ WisePenView\src\components\Note\CustomBlockNote\LatexSupport\InlineMath\index.tsx    # 行内公式组件
├─ WisePenView\src\components\Drive\Modals\index.ts    # 文件区弹窗导出入口
├─ WisePenView\src\components\Note\CustomBlockNote\LatexSupport\LatexEditPopover\index.tsx    # LaTeX 编辑弹层组件
├─ WisePenView\src\components\Drive\FlatDrive\FileFilter\AddStickerModal\index.type.ts    # 添加贴纸弹窗类型定义
├─ WisePenView\src\components\Drive\FlatDrive\FileFilter\AddStickerModal\index.tsx    # 添加贴纸弹窗组件
├─ WisePenView\src\components\Drive\Modals\types.ts    # 文件区弹窗类型定义
├─ WisePenView\src\components\Note\CustomBlockNote\LatexSupport\MathBlock\style.module.less    # 数学块组件样式
├─ WisePenView\src\components\Note\CustomBlockNote\LatexSupport\MathBlock\index.tsx    # 数学块组件
├─ WisePenView\src\components\Drive\TreeDrive\FolderDrive\index.tsx    # 文件夹驱动树组件
├─ WisePenView\src\components\Drive\Modals\NewFolderModal\index.type.ts    # 新建文件夹弹窗类型定义
├─ WisePenView\src\components\Drive\Modals\NewFolderModal\index.tsx    # 新建文件夹弹窗组件
├─ WisePenView\src\components\Drive\Modals\NewFolderModal\index.module.less    # 新建文件夹弹窗样式
├─ WisePenView\src\components\Drive\Modals\UploadFileToGroupModal\index.type.ts    # 上传到组织弹窗类型定义
├─ WisePenView\src\components\Drive\Modals\UploadFileToGroupModal\index.tsx    # 上传到组织弹窗组件
├─ WisePenView\src\components\Drive\Modals\UploadFileToGroupModal\index.module.less    # 上传到组织弹窗样式
├─ WisePenView\src\components\Drive\Modals\RenameFolderModal\index.type.ts    # 重命名文件夹弹窗类型定义
├─ WisePenView\src\components\Drive\Modals\RenameFolderModal\index.tsx    # 重命名文件夹弹窗组件
├─ WisePenView\src\components\Drive\Modals\RemoveFileFromGroupModal\index.type.ts    # 从组织移除文件弹窗类型定义
├─ WisePenView\src\components\Drive\Modals\RemoveFileFromGroupModal\index.tsx    # 从组织移除文件弹窗组件
├─ WisePenView\src\components\Drive\Modals\EditStickerModal\index.type.ts    # 编辑贴纸弹窗类型定义
├─ WisePenView\src\components\Drive\Modals\EditStickerModal\index.tsx    # 编辑贴纸弹窗组件
├─ WisePenView\src\components\Drive\Modals\EditStickerModal\index.module.less    # 编辑贴纸弹窗样式
├─ WisePenView\src\components\Drive\Modals\NewTagModal\index.type.ts    # 新建标签弹窗类型定义
├─ WisePenView\src\components\Drive\Modals\NewTagModal\index.tsx    # 新建标签弹窗组件
├─ WisePenView\src\components\Drive\Modals\NewTagModal\index.module.less    # 新建标签弹窗样式
├─ WisePenView\src\components\Drive\Modals\EditTagModal\index.type.ts    # 编辑标签弹窗类型定义
├─ WisePenView\src\components\Drive\Modals\EditTagModal\index.tsx    # 编辑标签弹窗组件
├─ WisePenView\src\components\Drive\Modals\EditTagModal\index.module.less    # 编辑标签弹窗样式
├─ WisePenView\src\components\Drive\Modals\RenameFileModal\index.type.ts    # 重命名文件弹窗类型定义
├─ WisePenView\src\components\Drive\Modals\RenameFileModal\index.tsx    # 重命名文件弹窗组件
├─ WisePenView\src\components\Drive\Modals\StickerManageModal\style.module.less    # 贴纸管理弹窗样式
├─ WisePenView\src\components\Drive\Modals\StickerManageModal\index.type.ts    # 贴纸管理弹窗类型定义
├─ WisePenView\src\components\Drive\Modals\StickerManageModal\index.tsx    # 贴纸管理弹窗组件
├─ WisePenView\src\components\Drive\Modals\MoveToFolderModal\index.type.ts    # 移动到文件夹弹窗类型定义
├─ WisePenView\src\components\Drive\Modals\MoveToFolderModal\index.tsx    # 移动到文件夹弹窗组件
├─ WisePenView\src\components\Drive\Modals\MoveToFolderModal\index.module.less    # 移动到文件夹弹窗样式
├─ WisePenView\src\components\Drive\Modals\RenameTagModal\index.type.ts    # 重命名标签弹窗类型定义
├─ WisePenView\src\components\Drive\Modals\RenameTagModal\index.tsx    # 重命名标签弹窗组件
├─ WisePenView\src\components\Drive\Modals\DeleteFolderModal\index.type.ts    # 删除文件夹弹窗类型定义
├─ WisePenView\src\components\Drive\Modals\DeleteFolderModal\index.tsx    # 删除文件夹弹窗组件
├─ WisePenView\src\components\Drive\Modals\DeleteFileModal\index.type.ts    # 删除文件弹窗类型定义
├─ WisePenView\src\components\Drive\Modals\DeleteFileModal\index.tsx    # 删除文件弹窗组件
├─ WisePenView\src\components\Drive\Modals\DeleteTagModal\index.type.ts    # 删除标签弹窗类型定义
└─ WisePenView\src\components\Drive\Modals\DeleteTagModal\index.tsx    # 删除标签弹窗组件
```

## 覆盖校验
- src 实际文件总数：410
- 文档条目总数：410
- 校验结果：通过
- 二进制处理方式：对二进制文件读取文件头/元数据（本项目为 PNG 图片，已通过 `view` 读取图像头信息与尺寸等元信息）。

