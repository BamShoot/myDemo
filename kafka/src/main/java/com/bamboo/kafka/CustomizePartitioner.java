package com.bamboo.kafka;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;

import java.util.Map;

/**
 *@Author xu.xudong
 *@Date 2020/9/29 9:08
 */


public class CustomizePartitioner implements Partitioner {
    /**
     *  分区策略：kafka中每个topic被划分为多个分区，生产者将消息发送到topic时，内容被保存到哪个分区
     *
     *  路由机制：
     *      1、若发送消息时指定了分区（即自定义分区策略），则直接将消息append到指定分区
     *      2、若发送消息时未指定 patition，但指定了 key（kafka允许为每条消息设置一个key），
     *          则对key值进行hash计算，根据计算结果路由到指定分区，这种情况下可以保证同一个 Key 的所有消息都进入到相同的分区；
     *      3、patition 和 key 都未指定，则使用kafka默认的分区策略，轮询选出一个分区
     *
     *  自定义分区策略：
     *      1、新建一个分区器类实现Partitioner接口，重写方法，其中partition方法的返回值就表示将消息发送到几号分区
     *      2、在application.propertise中配置自定义分区器，配置的值就是分区器类的全路径名：
     *          spring.kafka.producer.properties.partitioner.class=com.felix.kafka.producer.CustomizePartitioner
     */


    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        return 0;
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> configs) {

    }

}
