package com.oriole.wisepen.resource.domain.dto.req;

import com.oriole.wisepen.resource.enums.SearchScope;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

/**
 * 前端全文搜索请求 DTO。
 * <p>
 * 字段命名 (page/size) 与 {@code ResourceItemController#listResources} 保持一致，
 * 便于前端复用同一套分页客户端逻辑。
 */
@Data
public class SearchQueryReqDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "搜索关键字不能为空")
    private String keyword;

    /** 搜索范围：默认 {@link SearchScope#ALL}，与前端"全部"Tab 对齐 */
    private SearchScope scope = SearchScope.ALL;

    @Min(value = 1, message = "页码不能小于 1")
    private Integer page = 1;

    /** 单页大小：滚动加载常用步长，下限 1、上限 100，默认 20 */
    @Min(value = 1, message = "每页数量不能小于 1")
    @Max(value = 100, message = "每页数量不能大于 100")
    private Integer size = 20;
}
