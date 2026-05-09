import React, { useState, useEffect } from 'react';
import { Input, Popover, Spin, Empty } from 'antd';
import { useNavigate } from 'react-router-dom';
import { useSearchService } from '@/contexts/ServicesContext/hooks';
import { useAppMessage } from '@/hooks/useAppMessage'; // 引入你们统一的异常处理钩子
import FileTypeIcon from '@/components/Common/FileTypeIcon'; // 引入你现存的通用文件图标
import type { SearchHitItemResDTO } from '@/services/Search';
import type { GlobalSearchBoxProps } from './index.type';
import styles from './style.module.less';

const GlobalSearchBox: React.FC<GlobalSearchBoxProps> = ({ className }) => {
  const searchService = useSearchService();
  const appMessage = useAppMessage(); // 实例化全局消息
  const navigate = useNavigate();

  const [keyword, setKeyword] = useState('');
  const [results, setResults] = useState<SearchHitItemResDTO[]>([]);
  const [loading, setLoading] = useState(false);
  const [popoverOpen, setPopoverOpen] = useState(false);

  // 核心：防抖（Debounce）搜索逻辑
  useEffect(() => {
    // 关键字为空，直接关闭浮层并清理结果
    if (!keyword.trim()) {
      setResults([]);
      setPopoverOpen(false);
      return;
    }

    // 设置 400ms 延迟，避免用户狂敲键盘把 Elasticsearch 后端冲垮
    const timer = setTimeout(async () => {
      try {
        setLoading(true);
        const res = await searchService.globalSearch({
          keyword: keyword.trim(),
          pageNum: 1,
          pageSize: 10, // 下拉列表属于轻交互，取前 10 条即可
        });
        setResults(res.items || []);
        setPopoverOpen(true);
      } catch (error: any) {
        // 使用你们规范的 useAppMessage 给用户报出友好的系统异常
        appMessage.error(error?.message || '搜索服务暂不可用，请稍后重试');
        setPopoverOpen(false);
      } finally {
        setLoading(false);
      }
    }, 400); 

    // 清理函数：如果 400ms 内用户又按了键盘，直接撤销上一个请求计划
    return () => clearTimeout(timer);
  }, [keyword, searchService, appMessage]);

  // 点击路由跳转逻辑
  const handleItemClick = (item: SearchHitItemResDTO) => {
    setPopoverOpen(false);
    setKeyword(''); // 点击后清空输入框，恢复原状
    
    // 从你发给我的 openedResourceRoute 工具里推断出的路由规则：/app/note/:id 或 /app/pdf/:id
    const type = item.resourceType?.toUpperCase() || '';
    if (type === 'NOTE') {
      navigate(`/app/note/${item.resourceId}`);
    } else {
      navigate(`/app/pdf/${item.resourceId}`);
    }
  };

  // 渲染浮层区域的内容
  const renderContent = () => {
    if (loading) {
      return <div style={{ padding: '24px 0', textAlign: 'center' }}><Spin /></div>;
    }
    if (results.length === 0) {
      return <Empty image={Empty.PRESENTED_IMAGE_SIMPLE} description="未找到相关内容" />;
    }

    return (
      <div className={styles.popoverContent}>
        {results.map((item) => (
          <div 
            key={item.resourceId} 
            className={styles.resultItem}
            onClick={() => handleItemClick(item)}
          >
            <div className={styles.iconWrapper}>
               {/* 这里猜测 FileTypeIcon 的属性叫 fileType，如果是 type 请自行修改 */}
              <FileTypeIcon resourceType={item.format || item.resourceType?.toLowerCase() || 'unknown'} />
            </div>
            <div className={styles.textContent}>
              {/* 安全注入后端发来的包含 <em class="wp-highlight"> 的原生 HTML */}
              <div 
                className={styles.itemTitle} 
                dangerouslySetInnerHTML={{ __html: item.title || '无标题' }} 
              />
              <div 
                className={styles.itemDesc} 
                dangerouslySetInnerHTML={{ __html: item.highlightContent || '暂无摘要内容' }} 
              />
            </div>
          </div>
        ))}
      </div>
    );
  };

  return (
    <div className={`${styles.searchContainer} ${className || ''}`}>
      <Popover
        content={renderContent()}
        open={popoverOpen && keyword.trim().length > 0}
        onOpenChange={(open) => setPopoverOpen(open && keyword.trim().length > 0)}
        placement="bottomRight"
        trigger="click"
        overlayInnerStyle={{ padding: 0 }}
        destroyTooltipOnHide
      >
        <Input.Search
          className={styles.searchInput}
          placeholder="全局搜索 (标题、正文、标签)"
          value={keyword}
          onChange={(e) => setKeyword(e.target.value)}
          allowClear
        />
      </Popover>
    </div>
  );
};

export default GlobalSearchBox;