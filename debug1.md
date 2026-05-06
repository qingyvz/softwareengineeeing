太不容易了！看着你一步步从网络报错、查到编译配置，再挖到微服务最底层的安全拦截器，这绝对是一次**史诗级的高级 Debug 实战**！

### 先回答你关于 Nacos 的问题：你没看错！

从你发的两张截图来看，这里有两个非常关键的信息：
1. **你的搜索服务确实还没上 Nacos：** 截图中全是 `system`, `user`, `document`, `resource` 等老服务，**根本没有 `wisepen-search-service-dev.yaml`**。因为这是你自己新写的微服务，还没去 Nacos 控制台建配置文件，所以它目前完全依赖你本地的 `application-dev.yml`。
2. **那个拦截器的密码藏在哪里？** 拦截器的配置类在 `common` 包里，所以即使配了密码，也一定在 **`wisepen-common-dev.yaml`** 里（截图1的第二个就是它）。
   * 不过，**有没有配已经不重要了！** 因为代码里写了 `@Value("${wisepen.security.from-source:APISIX-wX0iR6tY}")`。冒号后面的就是**保底默认值**。既然你没特意去改，那个暗号就是铁打的 `APISIX-wX0iR6tY`！

---

下面为你生成一份专属的 **Markdown 格式的“踩坑与破局”总结报告**。你可以直接把这段存进你的个人笔记（Obsidian、Notion 或语雀）里，以后写进简历或者面试时拿出来吹牛，绝对是加分项！

***

# 📝 记一次微服务网关鉴权与本地 404 (0字节) 排查实战

## 📌 背景
在开发基于 APISIX + Nacos + Spring Boot 的微服务架构项目时，本地新建了一个 `wisepen-search-service` 搜索服务，并编写了最简单的测试接口 `GET /search/ping`。但在使用 Postman 联调时，遭遇了连环报错。

## 🚨 阶段一：走网关报错 `ECONNREFUSED`
* **现象**：按项目规范请求网关 `http://wisepen-dev-server:9080/search/ping`，并在 Header 加了联调标识 `X-Developer: mbzhou24`，结果报错 `ECONNREFUSED` (连接被拒绝)。
* **排查**：
  1. 检查本地 IP，确认 Nacos 注册的本地服务 IP (`10.230.35.42`) 完全正确。
  2. 交叉测试，发现远程网关的控制面板 (`9000` 端口) 可以访问，唯独流量代理层 (`9080` 端口) 挂了。
  3. 结合项目的《开发公告》，确认是运维在“服务器迁移”过程中，漏起了网关的数据面容器/未放行防火墙。
* **决策**：在服务器修好前，**决定绕过网关，直接请求本地服务端口进行开发测试**。

## 🚨 阶段二：直连本地报错 404 (Size: 0.00 B)
* **现象**：将请求改为直连本地 `http://localhost:9310/search/ping`，却返回了 404，且响应体大小为极度异常的 **0.00 B**。
* **排查过程**：
  1. **排查编译问题**：检查 `target/classes` 目录，确认 `.class` 文件已生成（排除了 IDEA 编译缓存问题）。
  2. **排查配置前缀**：查阅本地 `application-dev.yml` 与启动日志 `Tomcat started on port 9310 with context path ''`，确认端口为 `9310` 且无上下文前缀。URL 拼接无误。
  3. **存活测试 (关键突破)**：在 Controller 中加入 `@PostConstruct` 打印日志，启动时成功打印。这证明代码无误，Spring 已将路由加载到内存。
  4. **抓包分析**：控制台打印了 `Initializing Servlet 'dispatcherServlet'`，说明请求已进入 Spring，但被半路掐断，未抛出标准 JSON 错误（导致 0 字节）。**锁定嫌疑人：全局拦截器/过滤器**。

## 🕵️‍♂️ 阶段三：源码溯源，真凶落网
利用全局搜索，在项目的公共底层模块 (`wisepen-common`) 中找到了一个名为 `HeaderInterceptor` 的全局拦截器。

### 1. 拦截器的作案手法
架构师为了防止黑客绕过 APISIX 网关直接攻击微服务端口，写了如下防御逻辑：
```java
// 校验请求头，如果不包含约定的网关暗号，直接 404 且 return false
if (!fromSource.equals(request.getHeader("X-From-Source"))) {
    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    return false; // 掐断链路，导致 0 字节返回
}
```

### 2. 找到网关“接头暗号”
追踪注入该拦截器的配置类 `WisepenWebAutoConfiguration`，发现密码通过 `@Value` 注入，且附带了默认值：
```java
@Value("${wisepen.security.from-source:APISIX-wX0iR6tY}")
private String fromSource;
```

## 💡 最终解决方案 (2选1)

### 方案 A：优雅伪装法（推荐）
既然拦截器只认死理，我们在 Postman 模拟直连时，主动伪造网关的请求头：
* **URL**: `http://localhost:9310/search/ping`
* **Headers**:
  * `X-From-Source` : `APISIX-wX0iR6tY`  *(伪装网关通行证)*
  * `X-User-Id` : `1` *(如果接口有 `@CheckLogin` 鉴权，附带此头伪装已登录用户)*

### 方案 B：本地白名单法 (修改配置类)
在 `WisepenWebAutoConfiguration` 的 `addInterceptors` 方法中，将本地开发测试的接口加入免检白名单：
```java
registry.addInterceptor(new HeaderInterceptor(fromSource))
        .addPathPatterns("/**")
        .excludePathPatterns(
                "/auth/login",
                "/search/ping"  // <-- 将自己的测试接口加入白名单
        );
```

## 🎯 总结与反思
1. **0 字节的 404 绝非寻常**：标准的 Spring Boot 404 会返回 JSON 错误栈，遇到 0 字节 404，第一时间应怀疑 Filter/Interceptor 的 `setStatus()` 强行拦截。
2. **底层 common 包的威力**：微服务中，底层依赖库的自动配置类 (`@Configuration`) 会在不知不觉中接管所有上层业务模块的安全规则。
3. **架构视野**：理解了 APISIX 网关在微服务中不仅仅是转发流量，还承担着通过 Header 透传鉴权信息、隔离外部直连攻击的核心职责。

***

*(直接把上面的内容复制走吧，这是你靠自己的排查一步步赢来的战果！祝你接下来的搜索功能开发一切顺利！)*