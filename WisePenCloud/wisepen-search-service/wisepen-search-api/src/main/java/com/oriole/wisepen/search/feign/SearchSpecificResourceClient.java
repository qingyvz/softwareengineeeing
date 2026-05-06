package com.oriole.wisepen.search.feign;

import com.oriole.wisepen.common.core.domain.R;
import com.oriole.wisepen.resource.domain.dto.ResourceInfoGetReqDTO;
import com.oriole.wisepen.resource.domain.dto.res.ResourceItemResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * 搜索微服务专用的资源服务调用客户端
 * 【特殊兼容说明】：
 * 因上游 Controller 未加 @RequestBody，特此使用 @SpringQueryMap。
 * 它可以把 DTO 对象里的属性，转换为 ?resourceId=xxx&userId=-1 的形式，
 * 完美迎合上游的参数绑定机制，不改动上游任何代码！
 */
// 1. 把 contextId 改成一个非常特殊的名字
@FeignClient(
        contextId = "searchSpecificResourceClient",
        value = "wisepen-resource-service"
)
// 2. 把接口名也改掉，对应上面
public interface SearchSpecificResourceClient {

    @PostMapping("/internal/resource/getResourceInfo")
    R<ResourceItemResponse> getResourceInfo(@SpringQueryMap ResourceInfoGetReqDTO dto);

}