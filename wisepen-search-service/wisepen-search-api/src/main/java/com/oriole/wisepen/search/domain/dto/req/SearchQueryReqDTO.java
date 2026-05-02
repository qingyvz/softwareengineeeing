package com.oriole.wisepen.search.api.domain.dto.req;

import com.oriole.wisepen.search.api.enums.SearchTargetType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

/**
 * 前端全局搜索请求 DTO
 */
@Data
public class SearchQueryReqDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 搜索关键字
     */
    @NotBlank(message = "搜索关键字不能为空")
    private String keyword;

    /**
     * 搜索目标类型 (可选，如果不传则搜索全部，如 NOTE, DOCUMENT)
     */
    private SearchTargetType targetType;

    /**
     * 页码
     */
    @Min(value = 1, message = "页码不能小于1")
    private Integer pageNum = 1;

    /**
     * 每页数量
     */
    @Min(value = 1, message = "每页数量不能小于1")
    @Max(value = 100, message = "每页数量不能大于100")
    private Integer pageSize = 10;
}
