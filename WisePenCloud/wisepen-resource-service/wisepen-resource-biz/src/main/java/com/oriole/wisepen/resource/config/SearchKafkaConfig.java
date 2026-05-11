package com.oriole.wisepen.resource.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;

import java.util.Map;

/**
 * 搜索模块专用 Kafka 监听器工厂：使用纯 String 反序列化以隔离全局 JSON 反序列化器。
 * <p>
 * 配套消费者：{@code DocumentReadyConsumer} 与 {@code NoteSnapshotConsumer}，
 * 它们消费 Document / Note 服务发布的"裸字符串 JSON"（无 {@code __TypeId__} 头），
 * 因此不能复用全局 JsonDeserializer 工厂。groupId 由各 @KafkaListener 注解直接指定。
 */
@Configuration
public class SearchKafkaConfig {

    @Bean("searchRawStringContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, String> searchRawStringContainerFactory(
            KafkaProperties kafkaProperties) {
        Map<String, Object> props = kafkaProperties.buildConsumerProperties(null);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        DefaultKafkaConsumerFactory<String, String> consumerFactory = new DefaultKafkaConsumerFactory<>(props);

        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.RECORD);
        return factory;
    }
}
