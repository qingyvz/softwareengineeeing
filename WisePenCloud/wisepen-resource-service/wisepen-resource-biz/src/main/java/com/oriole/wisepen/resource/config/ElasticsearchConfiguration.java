package com.oriole.wisepen.resource.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
 * Elasticsearch 仓储扫描配置。
 * <p>
 * 仅显式扫描资源域仓储包，避免 Spring Data Elasticsearch 与 MongoDB Repository 互相抢扫描；
 * 底层 RestClient / 序列化配置交给 spring-boot-starter-data-elasticsearch 自动装配。
 */
@Configuration
@EnableElasticsearchRepositories(basePackages = "com.oriole.wisepen.resource.repository")
public class ElasticsearchConfiguration {
}
