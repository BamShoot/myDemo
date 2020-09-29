package com.bamboo.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 *@Author xu.xudong
 *@Date 2020/9/28 16:41
 */
@RestController
public class KafkaProducer {

    /**
     *  KafkaTemplate泛型中的第一个值指向Key，第二个指向存放的数据
     */
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;


    @GetMapping("/kafka/normal/{message}")
    public void sendMessage(@PathVariable("message") String message) {
        kafkaTemplate.send(KafkaConfig.TOPIC1, message);
    }

    /**
     *  kafkaTemplate提供了一个回调方法addCallback，我们可以在回调方法中监控消息是否发送成功 或 失败时做补偿处理
     */
    public void sendWithCallBack(String message) {
        kafkaTemplate.send(KafkaConfig.TOPIC1, message).addCallback(success -> {
            // 消息发送到的topic
            assert success != null;
            String topic = success.getRecordMetadata().topic();
            // 消息发送到的分区
            int partition = success.getRecordMetadata().partition();
            // 消息在分区内的offset
            long offset = success.getRecordMetadata().offset();
            System.out.println("发送消息成功:" + topic + "-" + partition + "-" + offset);

        }, failure -> {
            System.out.println("发送消息失败:" + failure.getMessage());
        });
    }

    /**
     *  在发送消息时需要创建事务，可以使用 KafkaTemplate 的 executeInTransaction 方法来声明事务
     */
    public void sendWithTransaction(String message) {
        // 声明事务：后面报错消息不会发出去
        kafkaTemplate.executeInTransaction(operations -> {
            operations.send(KafkaConfig.TOPIC1, "test executeInTransaction");
            throw new RuntimeException("fail");
        });

        // 不声明事务：后面报错但前面消息已经发送成功了
        kafkaTemplate.send(KafkaConfig.TOPIC1, "test executeInTransaction");
        throw new RuntimeException("fail");
    }
}