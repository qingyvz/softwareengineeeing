import React, { useState } from 'react';
import { Input, Modal, Segmented } from 'antd';
import { useDebounce } from 'ahooks';
import { RiSearchLine } from 'react-icons/ri';
import {
  SEARCH_SCOPE,
  type SearchScope,
} from '@/services/Resource/index.type';
import SearchResultList from '../SearchResultList';
import type { SearchModalProps } from './index.type';
import styles from './style.module.less';

const SCOPE_OPTIONS: { label: string; value: SearchScope }[] = [
  { label: '全部', value: SEARCH_SCOPE.ALL },
  { label: '文档', value: SEARCH_SCOPE.DOCUMENT },
  { label: '笔记', value: SEARCH_SCOPE.NOTE },
];

/**
 * 全局搜索模态主体。
 *
 * 选择 antd `Modal` 而非自绘遮罩，免费拿到 Esc 关闭、点遮罩关闭、焦点陷阱与
 * scroll lock，无需自行维护 `useGlobalSearchToggle`/`useKeyboardNav` 中的 Esc 分支。
 *
 * - `destroyOnHidden` 让关闭时 Input/列表内部状态全部销毁，下次开起从空状态开始。
 * - 关键词经 `useDebounce` 400ms 节流后再传给数据 Hook，与旧实现 400ms 一致。
 */
const SearchModal: React.FC<SearchModalProps> = ({ open, onCancel }) => {
  const [rawKeyword, setRawKeyword] = useState('');
  const debouncedKeyword = useDebounce(rawKeyword, { wait: 400 });
  const [scope, setScope] = useState<SearchScope>(SEARCH_SCOPE.ALL);

  return (
    <Modal
      open={open}
      onCancel={onCancel}
      closable={false}
      footer={null}
      width={720}
      destroyOnHidden
      maskClosable
      keyboard
      className={styles.modal}
      styles={{ body: { padding: 0 } }}
    >
      <div className={styles.header}>
        <RiSearchLine className={styles.headerIcon} />
        <Input
          className={styles.input}
          variant="borderless"
          autoFocus
          placeholder="搜索文档、笔记和标签..."
          value={rawKeyword}
          onChange={(e) => setRawKeyword(e.target.value)}
          allowClear
        />
        <kbd
          className={styles.kbd}
          onClick={onCancel}
          title="关闭搜索"
        >
          Esc
        </kbd>
      </div>

      <div className={styles.tabs}>
        <Segmented<SearchScope>
          options={SCOPE_OPTIONS}
          value={scope}
          onChange={setScope}
          block
        />
      </div>

      <SearchResultList keyword={debouncedKeyword} scope={scope} onClose={onCancel} />
    </Modal>
  );
};

export default SearchModal;
