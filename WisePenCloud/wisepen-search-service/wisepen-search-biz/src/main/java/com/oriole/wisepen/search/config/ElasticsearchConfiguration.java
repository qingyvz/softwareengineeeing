package com.oriole.wisepen.search.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
 * Elasticsearch 配置类
 * 扫描指定包下的 Repository，方便后续拓展 ES 连接池属性等
 */
@Configuration
@EnableElasticsearchRepositories(basePackages = "com.oriole.wisepen.search.biz.repository")
public class ElasticsearchConfiguration {
    // 默认底层依赖 spring-boot-starter-data-elasticsearch 的自动装配。
    // 如果有证书、特殊的 RestClient 配置，将在这里拓展。
}
