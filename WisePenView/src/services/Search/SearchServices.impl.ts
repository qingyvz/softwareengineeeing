import Axios from '@/utils/Axios';
import type { ISearchService, SearchQueryReqDTO, SearchResultResDTO } from './index.type';

class SearchServiceImpl implements ISearchService {
    async globalSearch(data: SearchQueryReqDTO): Promise<SearchResultResDTO> {
        // 依据你的确认，暂时不加额外前缀。如后端实际路径有变（如 /search/globalSearch），只需在此处调整即可
        const res = await Axios.post<SearchResultResDTO>('/search/global', data);
        return res.data;
    }
}

export function createSearchServices(): ISearchService {
    return new SearchServiceImpl();
}