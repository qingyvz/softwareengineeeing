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
// 1. 改掉 contextId，避免和降级类冲突
@FeignClient(contextId = "searchResourceClient", value = "wisepen-resource-service")
// 2. 接口名字去掉 Fallback，它是个纯粹的 Client
public interface SearchResourceClient {

    @PostMapping("/internal/resource/getResourceInfo")
    R<ResourceItemResponse> getResourceInfo(@SpringQueryMap ResourceInfoGetReqDTO dto);

}