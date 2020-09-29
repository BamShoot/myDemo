package com.bamboo.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *@Author xu.xudong
 *@Date 2020/9/28 16:30
 */
@Configuration
public class KafkaConfig {

    public static final String TOPIC1 = "topic1";

    public static final String TOPIC2 = "topic2";

    @Bean
    public NewTopic init() {
        //创建一个名为topic1的消息类别并设置分区数为8，分区副本数为2
        return new NewTopic(TOPIC1, 8, (short) 2);
    }

    @Bean
    public NewTopic update() {
        // 如果要修改分区数，只需修改配置值重启项目即可
        // 修改分区数并不会导致数据的丢失，但是分区数只能增大不能减小
        return new NewTopic(TOPIC1, 10, (short) 2);
    }

    @Bean
    public NewTopic topic2() {
        return new NewTopic(TOPIC2, 1, (short) 1);
    }

}
