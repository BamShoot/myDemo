[toc]

# 安装kafka

- 需要 Java 运行环境，下载 [kafka](http://kafka.apache.org/downloads)，选择 scala 子选项
- linux环境执行 bin 文件下命令，windows 环境执行 bin/windows 文件下命令
- 启动 ZK ：`bin\windows\zookeeper-server-start.bat config\zookeeper.properties`
- 启动 kafka ：`bin\windows\kafka-server-start.bat config\server.properties`



# kafka 配置

## kafka 服务器配置

- broker.id：申明当前kafka服务器在集群中的==唯一ID==，需配置为integer
- listeners：申明当前kafka服务器监听的端口，默认9092端口。用于只有内网的服务可以访问 kafka ，`listeners: <协议名称>://<内网ip>:<端口>`  例如：`listeners: PLAINTEXT://192.168.0.4:9092`
- advertised_listeners：这组监听器是 Broker 用于对外发布的，比如在 docker 或阿里云主机上部署 kafka。

- zookeeper.connect：申明kafka所连接的zookeeper的地址 ，使用 kafka 高版本中自带zookeeper 默认即可
- log.dirs：kafka数据的存放地址，多个地址的话用逗号分割



# SpringBoot 中使用



## 依赖

```xml
<dependency>
    <groupId>org.springframework.kafka</groupId>
    <artifactId>spring-kafka</artifactId>
</dependency>
```



## 配置

```properties
#kafka地址 brokers集群地址用,隔开
spring.kafka.bootstrap-servers=127.0.0.1:9092,127.0.0.1:9093,127.0.0.1:9094

#生产者的配置，大部分我们可以使用默认的，这里列出几个比较重要的属性
#每批次发送消息的数量
spring.kafka.producer.batch-size=16
#发送失败重试次数
spring.kafka.producer.retries=0
#即32MB的批处理缓冲区
spring.kafka.producer.buffer-memory=33554432
#key序列化方式
spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer
spring.kafka.producer.value-serializer=org.apache.kafka.common.serialization.StringSerializer

#消费者的配置
#Kafka中没有初始偏移或如果当前偏移在服务器上不再存在时，默认区最新，有三个选项 
#latest：重置为分区中最新的offset(消费分区中新产生的数据)
#earliest：重置为分区中最小的offset
#none：只要有一个分区不存在已提交的offset,就抛出异常
spring.kafka.consumer.auto-offset-reset=latest
#是否开启自动提交
spring.kafka.consumer.enable-auto-commit=true
#自动提交的时间间隔
spring.kafka.consumer.auto-commit-interval=100
#key的解码方式
spring.kafka.consumer.key-deserializer=org.apache.kafka.common.serialization.StringDeserializer
#value的解码方式
spring.kafka.consumer.value-deserializer=org.apache.kafka.common.serialization.StringDeserializer
#在kafka/config文件的consumer.properties中有配置
spring.kafka.consumer.group-id=test-consumer-group
# 消费端监听的topic不存在时，项目启动会报错(关掉)
spring.kafka.listener.missing-topics-fatal=false
```

