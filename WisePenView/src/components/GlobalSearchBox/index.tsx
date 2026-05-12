import React, { useState } from 'react';
import { Button, Tooltip } from 'antd';
import { useKeyPress } from 'ahooks';
import { RiSearchLine } from 'react-icons/ri';
import SearchModal from './SearchModal';
import type { GlobalSearchBoxProps } from './index.type';
import styles from './style.module.less';

/**
 * 用户平台标识：模块加载时一次性计算，运行期不会变。
 * 仅用于决定触发器角标显示 `⌘ K` 还是 `Ctrl K`。
 */
const IS_MAC =
  typeof navigator !== 'undefined' && navigator.platform.toLowerCase().includes('mac');
const SHORTCUT_LABEL = IS_MAC ? '⌘ K' : 'Ctrl K';

/**
 * 全局搜索入口组件：放大镜触发器 + 受控 Modal。
 *
 * - `useKeyPress(['ctrl.k', 'meta.k'])` 在任意挂载处都能监听快捷键，
 *   挂在 Drive 页时 Drive 在前台即生效；模态再由 antd Modal 处理 Esc/遮罩关闭。
 * - 默认导出保持不变，老调用方 `import GlobalSearchBox from '...'` 无须改动。
 */
const GlobalSearchBox: React.FC<GlobalSearchBoxProps> = ({ className }) => {
  const [open, setOpen] = useState(false);

  useKeyPress(
    ['ctrl.k', 'meta.k'],
    (e) => {
      e.preventDefault();
      setOpen(true);
    },
    { exactMatch: true }
  );

  return (
    <>
      <Tooltip
        title={
          <span>
            打开全局搜索 <kbd className={styles.tooltipKbd}>{SHORTCUT_LABEL}</kbd>
          </span>
        }
      >
        <Button
          className={`${styles.trigger} ${className ?? ''}`}
          icon={<RiSearchLine />}
          onClick={() => setOpen(true)}
          aria-label="打开全局搜索"
        >
          <span className={styles.triggerLabel}>搜索</span>
          <kbd className={styles.triggerKbd}>{SHORTCUT_LABEL}</kbd>
        </Button>
      </Tooltip>
      <SearchModal open={open} onCancel={() => setOpen(false)} />
    </>
  );
};

export default GlobalSearchBox;
