import { useRecentFilesStore } from '@/store';
import { ResourceItemApi } from '../apis/ResourceApi';
import type { ListResourceItemsApiRequest } from '../apis/ResourceApi.type';
import type {
  GetGroupResourceRequest,
  GetUserResourcesRequest,
  IResourceService,
  RenameResourceRequest,
  ResourceListPage,
  SearchQueryReqDTO,
  SearchResultPage,
  UpdateResourceTagsRequest,
} from './index.type';
import { SEARCH_SCOPE, TAG_QUERY_LOGIC_MODE } from './index.type';

const requestResourceItemList = async (
  params: GetUserResourcesRequest,
  queryOverrides: Partial<ListResourceItemsApiRequest> = {}
): Promise<ResourceListPage> => {
  const query: ListResourceItemsApiRequest = {
    page: params.page,
    size: params.size,
    sortBy: params.sortBy,
    sortDir: params.sortDir,
    tagQueryLogicMode: params.tagQueryLogicMode ?? TAG_QUERY_LOGIC_MODE.OR,
    ...queryOverrides,
  };
  if (params.resourceType != null && params.resourceType !== '') {
    query.resourceType = params.resourceType;
  }
  if (params.tagIds != null && params.tagIds.length > 0) {
    query.tagIds = params.tagIds;
  }
  const d = await ResourceItemApi.listResources(query);
  return {
    list: d?.list ?? [],
    total: d?.total ?? 0,
    page: d?.page ?? params.page,
    size: d?.size ?? params.size,
    totalPage: d?.totalPage ?? 0,
  };
};

const getUserResources = async (params: GetUserResourcesRequest): Promise<ResourceListPage> => {
  return requestResourceItemList(params);
};

const getGroupResources = async (params: GetGroupResourceRequest): Promise<ResourceListPage> => {
  return requestResourceItemList(params, { groupId: params.groupId });
};

const renameResource = async (params: RenameResourceRequest): Promise<void> => {
  await ResourceItemApi.renameResource(params);
  // 重命名成功后，更新最近文件列表的文件名
  useRecentFilesStore.getState().updateFileName(params.resourceId, params.newName);
};

const updateResourceTags = async (params: UpdateResourceTagsRequest): Promise<void> => {
  await ResourceItemApi.changeResourceTags(params);
};

const globalSearch = async (params: SearchQueryReqDTO): Promise<SearchResultPage> => {
  const d = await ResourceItemApi.globalSearch({
    keyword: params.keyword,
    scope: params.scope ?? SEARCH_SCOPE.ALL,
    page: params.page,
    size: params.size,
  });
  return {
    list: d?.list ?? [],
    total: d?.total ?? 0,
    page: d?.page ?? params.page,
    size: d?.size ?? params.size,
    totalPage: d?.totalPage ?? 0,
  };
};

export const createResourceServices = (): IResourceService => ({
  getUserResources,
  getGroupResources,
  renameResource,
  updateResourceTags,
  globalSearch,
});
