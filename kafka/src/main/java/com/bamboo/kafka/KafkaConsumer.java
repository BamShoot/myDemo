package com.bamboo.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ConsumerAwareListenerErrorHandler;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *@author xu.xudong
 *@Date 2020/9/28 17:01
 */
@Component
@EnableScheduling
@Slf4j
public class KafkaConsumer {

    @Autowired
    private ConsumerFactory<String, Object> consumerFactory;

    @Autowired
    private KafkaListenerEndpointRegistry registry;

    /**
     *  消息监听 @KafkaListener
     *  属性说明：
     *      1、id：消费者Id
     *      2、groupId：消费组Id
     *      3、topics：监听的topic，可监听多个
     *      4、topicPartitions：可配置更加详细的监听信息，可指定topic、parition、offset监听
     *      5、topics和topicPartitions不能同时使用
     *
     *  例：监听topic1的0号分区，同时监听topic2的0号分区和topic2的1号分区里面offset从8开始的消息
     *      @KafkaListener(topicPartitions = {
     *          @TopicPartition(topic = "topic1", partitions = {"0"}),
     *          @TopicPartition(topic = "topic2", partitions = "0",
     *              partitionOffsets = @PartitionOffset(partition = "1", initialOffset = "8"))
     *     })
     */
    @KafkaListener(topics = "consumer2")
    public void onMessage(ConsumerRecord<String, Object> record) {
        try {
            System.out.println("简单消费：类别-" + record.topic() + "--分区-" + record.partition() + "--内容-" + record.value());
            int i = Integer.parseInt(record.value().toString());

        } catch (Exception e) {
            log.error("error: {}", e.getMessage(), e);
        }
    }

    /**
     *  批量消费：需要配置application.prpertise
     *      1、设置批量消费：spring.kafka.listener.type=batch
     *      2、批量消费每次最多消费多少条消息：spring.kafka.consumer.max-poll-records=50
     */
    @KafkaListener(id = "consumer2", groupId = "felix-group", topics = "consumer2")
    public void onMessage(List<ConsumerRecord<String, Object>> records) {
    }


    /**
     *  通过异常处理器，可以处理consumer在消费时发生的异常
     *      新建一个 ConsumerAwareListenerErrorHandler 类型的异常处理方法，用@Bean注入，
     *      BeanName默认就是方法名，然后我们将这个异常处理器的BeanName放到 @KafkaListener注解的errorHandler属性里面，
     *      当监听抛出异常的时候，则会自动调用异常处理器
     */
    @Bean
    public ConsumerAwareListenerErrorHandler consumerAwareErrorHandler() {
        return (message, exception, consumer) -> {
            log.error("消费异常，消息：{}",message.getPayload());
            //消费单条消息
            //ConsumerRecord payload = (ConsumerRecord)message.getPayload();

            //批量消费
            List<ConsumerRecord> payload = (List<ConsumerRecord>)message.getPayload();
            return null;
        };
    }

    @KafkaListener(topics = {"consumer2"}, errorHandler = "consumerAwareErrorHandler")
    public void onMessage1(ConsumerRecord<String, Object> record) throws Exception{
        int i = Integer.parseInt(record.value().toString());
    }

    @KafkaListener(topics = {KafkaConfig.TOPIC1}, errorHandler = "consumerAwareErrorHandler")
    public void onMessage2(List<ConsumerRecord<String, Object>> records) {
        records.forEach(record->Integer.parseInt(record.value().toString()));
    }


    /**
     *  消息过滤器可以在消息抵达consumer之前被拦截
     *      配置消息过滤只需要为 监听器工厂 配置一个RecordFilterStrategy（消息过滤策略），
     *      返回true的时候消息将会被抛弃，返回false时，消息能正常抵达监听容器
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> filterContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        // 被过滤的消息将被丢弃
        factory.setAckDiscarded(true);
        // 消息过滤策略，返回true消息则被过滤
        factory.setRecordFilterStrategy(consumerRecord -> Integer.parseInt(consumerRecord.value().toString()) % 2 != 0);
        return factory;
    }

    @KafkaListener(topics = "consumer2", containerFactory = "filterContainerFactory")
    public void onMessage2(ConsumerRecord<String, Object> record) {
        System.out.println(record.value());
    }

    /**
     *  消息转发
     *      应用A从TopicA获取到消息，经过处理后转发到TopicB，再由应用B监听处理消息，
     *      即一个应用处理完成后将该消息转发至其他应用，完成消息的转发
     *
     *   在SpringBoot集成Kafka实现消息的转发也很简单，只需要通过一个@SendTo注解，被注解方法的return值即转发的消息内容
     */
    @KafkaListener(topics = {"consumer2"})
    @SendTo("topic2")
    public String onMessage3(ConsumerRecord<?, ?> record) {
        return record.value() + "-forward message";
    }


    /**
     *  定时启动、停止监听器
     *      默认情况下，当消费者项目启动的时候，监听器就开始工作，监听消费发送到指定topic的消息，
     *      那如果不想让监听器立即工作，而是让它在我们指定的时间点开始工作，或在指定的时间点停止工作，该怎么处理
     *      1、禁止监听器自启动
     *      2、在类上添加注解 @EnableScheduling 声明
     *      3、创建两个定时任务，一个用来在指定时间点启动定时器，另一个在指定时间点停止定时器
     */
    // 监听器容器工厂(设置禁止KafkaListener自启动)
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> delayContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Object> container =
                new ConcurrentKafkaListenerContainerFactory<>();
        container.setConsumerFactory(consumerFactory);
        //禁止KafkaListener自启动
        container.setAutoStartup(false);
        return container;
    }


    @KafkaListener(id = "timingConsumer", topics = "consumer2", containerFactory = "delayContainerFactory")
    public void onMessage4(ConsumerRecord<?, ?> record) {
        System.out.println("消费成功：" + record.topic() + "-" + record.partition() + "-" + record.value());
    }

    //定时启动监听器
    @Scheduled(cron = "0 42 11 * * ?")
    public void start() {
        //"timingConsumer"是@KafkaListener注解后面设置的监听器ID,标识这个监听器
        if (!registry.getListenerContainer("timingConsumer").isRunning()) {
            registry.getListenerContainer("timingConsumer").start();
        }
        registry.getListenerContainer("timingConsumer").resume();
    }

    //定时停止监听器
    @Scheduled(cron = "0 45 11 * * ? ")
    public void shutDownListener() {
        System.out.println("关闭监听器...");
        registry.getListenerContainer("timingConsumer").pause();
    }

}