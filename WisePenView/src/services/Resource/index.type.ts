/**
 * Resource 相关 API 请求类型
 * 与 resource.openapi.json 对齐
 */

import type { ResourceItem } from '@/types/resource';

/** 资源列表分页（与 OpenAPI PageResultResourceItemResponse 一致） */
export interface ResourceListPage {
  list: ResourceItem[];
  total: number;
  page: number;
  size: number;
  totalPage: number;
}

/** ResourceService 接口：供依赖注入使用 */
export interface IResourceService {
  getUserResources(params: GetUserResourcesRequest): Promise<ResourceListPage>;
  getGroupResources(params: GetGroupResourceRequest): Promise<ResourceListPage>;
  renameResource(params: RenameResourceRequest): Promise<void>;
  updateResourcePath(params: UpdateResourcePathRequest): Promise<void>;
  updateResourceTags(params: UpdateResourceTagsRequest): Promise<void>;
  globalSearch(params: SearchQueryReqDTO): Promise<SearchResultPage>;
}

/** 搜索范围：与后端 SearchScope 枚举字面值保持一致 */
export const SEARCH_SCOPE = { ALL: 'ALL', DOCUMENT: 'DOCUMENT', NOTE: 'NOTE' } as const;
export type SearchScope = (typeof SEARCH_SCOPE)[keyof typeof SEARCH_SCOPE];

/** 全文搜索请求 DTO，对齐后端 POST /search/global 入参 */
export interface SearchQueryReqDTO {
  keyword: string;
  scope?: SearchScope;
  page: number;
  size: number;
}

/**
 * 单条搜索命中项；字段命名与后端 SearchHitItemResDTO 一致，便于零映射直读。
 *
 * - resourceType: 小写枚举字面值（pdf / docx / note / unknown ...）
 * - resourceName / highlightContent: 可能携带 <em class="wp-highlight"> 包裹，
 *   渲染端需经 dangerouslySetInnerHTML 才能让高亮生效
 */
export interface SearchHitItemResDTO {
  resourceId: string;
  resourceType: string;
  resourceName: string;
  highlightContent: string | null;
  updateTime: string;
}

/** 搜索分页结果，结构与 PageResult<SearchHitItemResDTO> 序列化形态一致 */
export interface SearchResultPage {
  list: SearchHitItemResDTO[];
  total: number;
  page: number;
  size: number;
  totalPage: number;
}

/** 排序字段枚举 */
export const RESOURCE_SORT_BY = {
  UPDATE_TIME: 'UPDATE_TIME',
  CREATE_TIME: 'CREATE_TIME',
  NAME: 'NAME',
  SIZE: 'SIZE',
} as const;

/** 排序方向枚举 */
export const RESOURCE_SORT_DIR = {
  ASC: 'ASC',
  DESC: 'DESC',
} as const;

/** 标签查询逻辑：OR=包含任意标签，AND=包含全部标签 */
export const TAG_QUERY_LOGIC_MODE = {
  OR: 'OR',
  AND: 'AND',
} as const;

export type TagQueryLogicMode = (typeof TAG_QUERY_LOGIC_MODE)[keyof typeof TAG_QUERY_LOGIC_MODE];

export type ResourceSortBy = (typeof RESOURCE_SORT_BY)[keyof typeof RESOURCE_SORT_BY];
export type ResourceSortDir = (typeof RESOURCE_SORT_DIR)[keyof typeof RESOURCE_SORT_DIR];

/** 重命名资源请求参数（对齐 OpenAPI ResourceRenameRequest，POST /resource/item/renameRes） */
export interface RenameResourceRequest {
  resourceId: string;
  newName: string;
}

/**
 * 更新资源归属路径（移动文件到文件夹）
 * 注意：resource.openapi.json 当前未声明该能力，实现层仍请求 /resource/move
 */
export interface UpdateResourcePathRequest {
  resourceId: string;
  /** 目标路径，如 '/' 或 '/documents/notes' */
  path: string;
  groupId?: string;
}

/** 更新资源用户标签（对齐 OpenAPI ResourceUpdateTagsRequest，POST /resource/item/updateTags） */
export interface UpdateResourceTagsRequest {
  resourceId: string;
  tagIds: string[];
  groupId?: string;
}

/**
 * 获取用户资源列表请求参数（个人所有资源，group 不暴露、强制留空）
 * 对齐 GET /resource/item/list 的 query 参数
 */
export interface GetUserResourcesRequest {
  page: number;
  size: number;
  sortBy: (typeof RESOURCE_SORT_BY)[keyof typeof RESOURCE_SORT_BY];
  sortDir: (typeof RESOURCE_SORT_DIR)[keyof typeof RESOURCE_SORT_DIR];
  resourceType?: string;
  /** 按标签筛选，传 tagId 列表 */
  tagIds?: string[];
  /** 标签查询逻辑：OR=包含任意，AND=包含全部 */
  tagQueryLogicMode?: TagQueryLogicMode;
}

/** 获取小组资源列表请求参数（groupId 必填） */
export type GetGroupResourceRequest = GetUserResourcesRequest & {
  groupId: string;
};
