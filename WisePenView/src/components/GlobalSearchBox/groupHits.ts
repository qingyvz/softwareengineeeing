import type { SearchHitItemResDTO, SearchScope } from '@/domains/Resource';

/**
 * 资源类型字面值 → 分组 Header 标签的映射。
 *
 * 后端 ResourceType 枚举走 `@JsonValue` 序列化为小写扩展名，
 * 这里仅做对应的展示字符串映射。未来后端新增类型，命中此映射即可显示友好标签，
 * 否则降级为 `extension.toUpperCase()`。
 */
const TYPE_LABELS: Readonly<Record<string, string>> = {
  pdf: 'PDF',
  doc: 'DOC',
  docx: 'DOCX',
  ppt: 'PPT',
  pptx: 'PPTX',
  xls: 'XLS',
  xlsx: 'XLSX',
  note: 'Note',
};

/** 单个分组：key 用作 React key 与稳定排序依据，label 用于 Header 渲染 */
export interface SearchHitGroup {
  key: string;
  label: string;
  items: SearchHitItemResDTO[];
}

/**
 * 把搜索命中按 scope 做防御性二次过滤、再按 resourceType 桶排。
 *
 * - DOCUMENT Tab 兜底剔除 note / unknown
 * - NOTE Tab 仅保留 note
 * - ALL Tab 剔除 unknown（占位类型不应展示）
 *
 * 保持后端命中顺序：Map 的插入序即各组的"首次出现序"，与后端打分相关性同向。
 */
export function groupHits(hits: SearchHitItemResDTO[], scope: SearchScope): SearchHitGroup[] {
  const filtered = hits.filter((hit) => {
    const type = (hit.resourceType ?? '').toLowerCase();
    if (scope === 'DOCUMENT') {
      return type !== 'note' && type !== 'unknown' && type !== '';
    }
    if (scope === 'NOTE') {
      return type === 'note';
    }
    return type !== 'unknown' && type !== '';
  });

  const bucketMap = new Map<string, SearchHitItemResDTO[]>();
  for (const hit of filtered) {
    const key = (hit.resourceType ?? '').toLowerCase() || 'unknown';
    const bucket = bucketMap.get(key);
    if (bucket) {
      bucket.push(hit);
    } else {
      bucketMap.set(key, [hit]);
    }
  }

  return [...bucketMap.entries()].map(([key, items]) => ({
    key,
    label: TYPE_LABELS[key] ?? key.toUpperCase(),
    items,
  }));
}

/** 把分组结构按行铺平，用于键盘导航的下标定位 */
export function flattenGroups(groups: SearchHitGroup[]): SearchHitItemResDTO[] {
  return groups.flatMap((group) => group.items);
}
