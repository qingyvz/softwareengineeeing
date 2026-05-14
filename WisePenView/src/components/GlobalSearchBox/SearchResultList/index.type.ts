import type { SearchHitItemResDTO, SearchScope } from '@/domains/Resource';

export interface SearchResultListProps {
  /** 已防抖的关键词，用于决定"未输入"与"无结果"两种空态文案 */
  keyword: string;
  scope: SearchScope;
  /**
   * 命中点击后通知父级关闭模态；路由跳转在本组件内完成，
   * 这样把"打开 → 关闭 → 跳转"的副作用聚合在单一回调里，避免父子各做一半。
   */
  onClose: () => void;
}
