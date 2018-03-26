# readme

---
- 基本信息
1.	使用了Maven进行项目管理
2.	用Git进行简单代码管理，代码已上传至Github。https://github.com/ouguozheng/personal/tree/master/LET
3. 引用第三方库：srping,slf4j,junit

- 实现功能
1. 支持get,post请求方式，
2. 支持动态增加参数
3. 每增加一个连接新增一个连接线程，提高处理效率
4. 建立session管理连接会话，定时检查过期会话，支持动态调整会话过期时间和检查会话过期检查时间
5. 通知日志记录服务连接信息
6. 可支持同时并发发送多个session
7. 支持异步发送
8. 有异常处理
9. 使用少量的循环，提交效率
10. 定义消息体
