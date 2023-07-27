### PainKiller
- Springboot工程 没有外部数据源 直接启动Application即可
- 请求ip:port/StressTest/go
- 示例参数: {
  "uri":"www.baidu.com",
  "method":"GET",
  "expectQps":10000,
  "requestCount":5000,
  "threads":400
  }
