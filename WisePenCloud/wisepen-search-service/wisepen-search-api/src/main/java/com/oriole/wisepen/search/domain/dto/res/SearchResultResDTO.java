package com.oriole.wisepen.search.domain.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import com.oriole.wisepen.search.domain.dto.res.SearchHitItemResDTO;
/**
 * 统一搜索结果响应 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchResultResDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 命中总条数
     */
    private Long total;

    /**
     * 搜索耗时 (毫秒)
     */
    private Long tookInMillis;

    /**
     * 当前页搜索结果列表
     */
    private List<SearchHitItemResDTO> items;
}