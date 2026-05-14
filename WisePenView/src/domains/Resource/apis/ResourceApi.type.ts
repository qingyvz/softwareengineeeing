import type { ResourceItem } from '@/domains/Resource';

export interface ResourceListPageApiResponse {
  list: ResourceItem[];
  total: number;
  page: number;
  size: number;
  totalPage: number;
}

export interface ListResourceItemsApiRequest {
  page: number;
  size: number;
  sortBy: string;
  sortDir: string;
  resourceType?: string;
  tagIds?: string[];
  tagQueryLogicMode?: string;
  groupId?: string;
}

export interface RenameResourceApiRequest {
  resourceId: string;
  newName: string;
}

export interface ChangeResourceTagsApiRequest {
  resourceId: string;
  tagIds: string[];
  groupId?: string;
}

export interface RemoveResourcesApiRequest {
  resourceIds: string[];
}

/** POST 全文搜索请求体，与后端 SearchQueryReqDTO 一致 */
export interface GlobalSearchApiRequest {
  keyword: string;
  scope?: string;
  page: number;
  size: number;
}

/** 全文搜索分页响应，与 PageResult<SearchHitItemResDTO> 一致 */
export interface GlobalSearchApiResponse {
  list: Array<{
    resourceId: string;
    resourceType: string;
    resourceName: string;
    highlightContent: string | null;
    updateTime: string;
  }>;
  total: number;
  page: number;
  size: number;
  totalPage: number;
}

export interface AddTagApiRequest {
  groupId?: string;
  parentId?: string;
  tagName: string;
  tagDesc?: string;
  visibilityMode?: string;
  specifiedUsers?: string[];
}

export interface ChangeTagApiRequest {
  groupId?: string;
  tagName?: string;
  tagDesc?: string;
  visibilityMode?: string;
  specifiedUsers?: string[];
  targetTagId: string;
}

export interface RemoveTagApiRequest {
  groupId?: string;
  targetTagId: string;
}

export interface MoveTagApiRequest {
  groupId?: string;
  targetTagId: string;
  newParentId?: string;
}

export interface GetTagTreeApiRequest {
  groupId?: string;
}

export interface TagTreeResponse {
  tagId: string;
  tagName: string;
  groupId?: string;
  tagDesc?: string;
  visibilityMode?: string;
  specifiedUsers?: string[];
  parentId?: string;
  children?: TagTreeResponse[];
}

export type GetTagTreeApiResponse = TagTreeResponse[];
