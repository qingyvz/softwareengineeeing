package com.oriole.wisepen.search.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.Map;

@Configuration
public class KafkaIsolationConfig {

    /**
     * 专属的“纯字符串”消费者工厂
     * 作用：彻底屏蔽 Nacos 和全局 ErrorHandler 的污染，强制按 String 读取
     */
    @Bean("rawStringContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, String> rawStringContainerFactory(
            KafkaProperties kafkaProperties) {

        // 1. 获取基础配置（保留 Nacos 里的 bootstrap-servers 等基础连接信息）
        Map<String, Object> props = kafkaProperties.buildConsumerProperties(null);

        // 2. ⚡ 强制覆盖核心配置（硬编码最高优先级）⚡
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        // 强制从最新消息开始，遗弃历史的 608 号毒药消息
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");

        // 3. 实例化干净的 ConsumerFactory
        DefaultKafkaConsumerFactory<String, String> cf = new DefaultKafkaConsumerFactory<>(props);

        // 4. 构建并返回监听器容器工厂
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(cf);
        // 关闭全局错误重试机制带来的死循环风险
        factory.getContainerProperties().setAckMode(org.springframework.kafka.listener.ContainerProperties.AckMode.RECORD);

        return factory;
    }
}