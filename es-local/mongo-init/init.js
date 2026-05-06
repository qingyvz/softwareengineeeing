// mongo-init/init.js
// 切换到具体的业务库（相当于 MySQL 的 USE）
db = db.getSiblingDB('wisepen_note');

// 插入测试用的笔记正文（对应 MySQL 的 ID）
db.note_document.insertMany([
    { noteId: NumberLong(1001), content: "今天我们学习了如何使用 Docker 部署带有 IK 分词器的 ES..." },
    { noteId: NumberLong(1002), content: "编写 docker-compose.yml 可以一键拉起整个后端基础设施..." },
    { noteId: NumberLong(1003), content: "分布式系统中，我们经常使用 SkyWalking 或 Tempo 来做..." }
]);