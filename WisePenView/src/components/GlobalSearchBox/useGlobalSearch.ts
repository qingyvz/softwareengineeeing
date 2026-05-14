import { useInfiniteScroll } from 'ahooks';
import type { RefObject } from 'react';
import { useResourceService } from '@/domains';
import { useAppMessage } from '@/hooks/useAppMessage';
import { parseErrorMessage } from '@/utils/parseErrorMessage';
import type { SearchHitItemResDTO, SearchScope } from '@/domains/Resource';

/** 单页大小：与后端 `@Max(100)` 上限一致，默认 20 是首屏滚动加载的舒适步长 */
export const PAGE_SIZE = 20;

export interface UseGlobalSearchOptions {
  /** 已经经过 400ms 防抖的关键词；空串时不发起请求 */
  keyword: string;
  scope: SearchScope;
  /** 可滚动容器的 ref，供 useInfiniteScroll 监听其 scrollTop 触发自动加载 */
  target: RefObject<HTMLElement | null>;
}

/** useInfiniteScroll 累积态：list 为累计命中，分页元信息用于判断是否到底 */
export interface SearchInfiniteData {
  list: SearchHitItemResDTO[];
  total: number;
  page: number;
  totalPage: number;
  isNoMore: boolean;
}

/**
 * 全局搜索数据 Hook。
 *
 * 设计要点：
 * - 直接以 ahooks `useInfiniteScroll` 承载分页/滚动监听/失败重试/竞态拦截，
 *   避免自行造轮子；`reloadDeps` 在 keyword 或 scope 变化时自动重置到 page 1。
 * - `manual: true` 在关键词为空时停止请求，避免输入清空瞬间打到后端。
 * - 错误统一走 `useAppMessage().error` toast，由 `parseErrorMessage` 兜底文案。
 */
export function useGlobalSearch({ keyword, scope, target }: UseGlobalSearchOptions) {
  const resourceService = useResourceService();
  const message = useAppMessage();

  const trimmed = keyword.trim();
  const isEmpty = trimmed.length === 0;

  return useInfiniteScroll<SearchInfiniteData>(
    async (current) => {
      // 第 N 次拉取的 page：用已累计条数推算，避免与 useInfiniteScroll 内部页码耦合
      const nextPage = current ? Math.floor(current.list.length / PAGE_SIZE) + 1 : 1;
      const res = await resourceService.globalSearch({
        keyword: trimmed,
        scope,
        page: nextPage,
        size: PAGE_SIZE,
      });
      return {
        list: res.list,
        total: res.total,
        page: res.page,
        totalPage: res.totalPage,
        isNoMore: nextPage >= Math.max(res.totalPage, 1),
      };
    },
    {
      target,
      isNoMore: (d) => !!d?.isNoMore,
      reloadDeps: [trimmed, scope],
      manual: isEmpty,
      onError: (err) => {
        message.error(parseErrorMessage(err, '搜索服务暂不可用，请稍后重试'));
      },
    }
  );
}
