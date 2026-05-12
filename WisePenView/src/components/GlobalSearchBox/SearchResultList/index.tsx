import React, { useCallback, useMemo, useRef, useState } from 'react';
import { Empty, Skeleton, Spin } from 'antd';
import { useKeyPress, useUpdateEffect } from 'ahooks';
import { useNavigate } from 'react-router-dom';
import FileTypeIcon from '@/components/Common/FileTypeIcon';
import { RESOURCE_TYPE } from '@/constants/resource';
import type { SearchHitItemResDTO } from '@/services/Resource/index.type';
import { useGlobalSearch } from '../useGlobalSearch';
import { flattenGroups, groupHits } from '../groupHits';
import type { SearchResultListProps } from './index.type';
import styles from './style.module.less';

/**
 * 搜索结果列表：分组渲染 + 无限滚动 + 键盘导航。
 *
 * - 滚动容器 `listRef` 同时是 useInfiniteScroll 的监听目标，触底自动 loadMore。
 * - 分组与铺平共用同一份 useMemo，保证下标与渲染一致。
 * - 高亮项采用「渲染期 clamp」而非"切换时重置"，规避在 effect 内 setState；
 *   destroyOnHidden 关掉模态后下一次开启会自然回到 0，体验完全一致。
 * - 命中标题与摘要均经后端 HtmlUtils.htmlEscape，仅保留 `<em class="wp-highlight">`，
 *   故 dangerouslySetInnerHTML 在此处是安全的。
 */
const SearchResultList: React.FC<SearchResultListProps> = ({ keyword, scope, onClose }) => {
  const listRef = useRef<HTMLDivElement>(null);
  const navigate = useNavigate();
  const { data, loading, loadingMore, noMore } = useGlobalSearch({
    keyword,
    scope,
    target: listRef,
  });

  const trimmedKeyword = keyword.trim();
  const groups = useMemo(() => groupHits(data?.list ?? [], scope), [data?.list, scope]);
  const flatItems = useMemo(() => flattenGroups(groups), [groups]);

  const [activeIndex, setActiveIndex] = useState(0);
  // 渲染期 clamp：当结果数量缩减时，下标自动回到最末行；为空时回 0
  const clampedActive = flatItems.length === 0
    ? 0
    : Math.min(activeIndex, flatItems.length - 1);

  // 高亮项滚入视口；block:nearest 避免列表反复跳到顶/底；纯 DOM 副作用，无 setState
  useUpdateEffect(() => {
    if (flatItems.length === 0) return;
    const container = listRef.current;
    if (!container) return;
    const row = container.querySelector<HTMLElement>(`[data-flat-index="${clampedActive}"]`);
    row?.scrollIntoView({ block: 'nearest' });
  }, [clampedActive, flatItems.length]);

  const handleOpenHit = useCallback(
    (item: SearchHitItemResDTO) => {
      onClose();
      // 与 useClickFile 同路由约定：note 走笔记编辑器，其余走 PDF 预览
      if (item.resourceType === RESOURCE_TYPE.NOTE) {
        navigate(`/app/note/${encodeURIComponent(item.resourceId)}`);
      } else {
        navigate(`/app/pdf/${encodeURIComponent(item.resourceId)}`);
      }
    },
    [navigate, onClose]
  );

  // ↑/↓ 跨分组移动 activeIndex；Enter 触发跳转。基于 clampedActive 计算保证不越界
  useKeyPress(
    'uparrow',
    (e) => {
      if (flatItems.length === 0) return;
      e.preventDefault();
      setActiveIndex(Math.max(0, clampedActive - 1));
    },
    { exactMatch: true }
  );
  useKeyPress(
    'downarrow',
    (e) => {
      if (flatItems.length === 0) return;
      e.preventDefault();
      setActiveIndex(Math.min(flatItems.length - 1, clampedActive + 1));
    },
    { exactMatch: true }
  );
  useKeyPress(
    'enter',
    () => {
      const item = flatItems[clampedActive];
      if (item) handleOpenHit(item);
    },
    { exactMatch: true }
  );

  // 空态优先级：未输入 > 加载中 > 命中为空 > 命中渲染
  const isFirstLoad = loading && !data;
  const hasHits = flatItems.length > 0;

  return (
    <div ref={listRef} className={styles.list}>
      {trimmedKeyword.length === 0 && (
        <div className={styles.emptyWrapper}>
          <Empty
            image={Empty.PRESENTED_IMAGE_SIMPLE}
            description="输入关键词搜索文档、笔记和标签"
          />
        </div>
      )}

      {trimmedKeyword.length > 0 && isFirstLoad && (
        <div className={styles.skeletonWrapper}>
          <Skeleton active paragraph={{ rows: 6 }} />
        </div>
      )}

      {trimmedKeyword.length > 0 && !isFirstLoad && !hasHits && (
        <div className={styles.emptyWrapper}>
          <Empty image={Empty.PRESENTED_IMAGE_SIMPLE} description="暂无相关结果" />
        </div>
      )}

      {hasHits && (
        <>
          {groups.map((group) => {
            return (
              <section key={group.key} className={styles.group}>
                <header className={styles.groupHeader}>{group.label}</header>
                <ul className={styles.groupItems}>
                  {group.items.map((item) => {
                    const flatIndex = flatItems.indexOf(item);
                    const active = flatIndex === clampedActive;
                    return (
                      <li
                        key={item.resourceId}
                        data-flat-index={flatIndex}
                        className={`${styles.row} ${active ? styles.rowActive : ''}`}
                        onMouseEnter={() => setActiveIndex(flatIndex)}
                        onClick={() => handleOpenHit(item)}
                      >
                        <div className={styles.rowIcon}>
                          <FileTypeIcon
                            resourceType={
                              item.resourceType === RESOURCE_TYPE.NOTE
                                ? RESOURCE_TYPE.NOTE
                                : RESOURCE_TYPE.FILE
                            }
                            size={18}
                          />
                        </div>
                        <div className={styles.rowText}>
                          <div
                            className={styles.rowTitle}
                            dangerouslySetInnerHTML={{
                              __html: item.resourceName || '无标题',
                            }}
                          />
                          {item.highlightContent && (
                            <div
                              className={styles.rowSnippet}
                              dangerouslySetInnerHTML={{
                                __html: item.highlightContent,
                              }}
                            />
                          )}
                        </div>
                      </li>
                    );
                  })}
                </ul>
              </section>
            );
          })}

          {loadingMore && (
            <div className={styles.loadingMore}>
              <Spin size="small" />
            </div>
          )}
          {!loadingMore && noMore && (
            <div className={styles.footerHint}>已展示全部结果</div>
          )}
        </>
      )}
    </div>
  );
};

export default SearchResultList;
