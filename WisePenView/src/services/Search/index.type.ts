export interface SearchQueryReqDTO {
    keyword: string;
    targetType?: 'DOCUMENT' | 'NOTE' | 'RESOURCE';
    pageNum: number;
    pageSize: number;
}

export interface SearchHitItemResDTO {
    resourceId: string;
    resourceType: string; // 后端传来可能是 "Note" 或 "Document"
    format?: string;      // 补上真实存在的字段 (pdf, docx, note)
    title: string;
    highlightContent: string;
    updateTime: string;
}

export interface SearchResultResDTO {
    total: number;
    tookInMillis: number;
    items: SearchHitItemResDTO[];
}

export interface ISearchService {
    globalSearch(data: SearchQueryReqDTO): Promise<SearchResultResDTO>;
}