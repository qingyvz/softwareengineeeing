//package com.oriole.wisepen.search.mq;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.oriole.wisepen.search.domain.mq.DocumentParseMsgDTO;
//import com.oriole.wisepen.search.domain.entity.SearchIndexEntity;
//import com.oriole.wisepen.search.service.ISearchSyncService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Component;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class DocumentParseTaskConsumer {
//
//    private final ISearchSyncService searchSyncService;
//    private final ObjectMapper objectMapper; // Spring 原生注入
//    private static final String TARGET_TYPE = "DOCUMENT";
//
//    @KafkaListener(topics = "topic_document_parsed", groupId = "wisepen-search-group")
//    public void onMessage(ConsumerRecord<String, String> record) {
//        try {
//            // 使用 Jackson 解析 JSON
//            DocumentParseMsgDTO msg = objectMapper.readValue(record.value(), DocumentParseMsgDTO.class);
//
//            if (msg.getDocumentId() == null) {
//                log.warn("文档解析消息缺少 documentId, 丢弃: {}", record.value());
//                return;
//            }
//
//            SearchIndexEntity entity = SearchIndexEntity.builder()
//                    .id(SearchIndexEntity.generateEsId(TARGET_TYPE, msg.getDocumentId()))
//                    .resourceId(msg.getDocumentId())
//                    .resourceType(TARGET_TYPE)
//                    .title(msg.getTitle())
//                    .content(msg.getFullText())
//                    .updateTime(msg.getUpdateTime())
//                    .build();
//
//            searchSyncService.saveOrUpdateIndex(entity);
//            log.info("文档索引构建成功: {}", entity.getId());
//
//        } catch (Exception e) {
//            log.error("消费文档解析消息发生异常, offset: {}", record.offset(), e);
//        }
//    }
//}