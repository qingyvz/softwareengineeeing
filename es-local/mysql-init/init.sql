-- mysql-init/init.sql
USE wisepen_cloud;

-- 模拟笔记服务的一张信息表
CREATE TABLE note_info (
    id BIGINT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    user_id BIGINT NOT NULL,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 插入几条假数据用于测试初始化游标
INSERT INTO note_info (id, title, user_id) VALUES 
(1001, 'Spring Boot 整合 Elasticsearch 指南', 1),
(1002, 'Docker 容器化部署实战', 2),
(1003, '微服务架构下的全链路追踪', 1);