package com.oriole.wisepen.search.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * 线程池配置类
 */
@Configuration
public class ThreadPoolConfig {

    @Bean
    public ScheduledExecutorService scheduledExecutorService() {
        // 创建一个核心线程数为 5 的定时任务线程池（数量你可以根据实际并发需求调整）
        return Executors.newScheduledThreadPool(5);
    }

}