package com.example.demo.mq.kafka;




import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.*;

import java.util.Date;
import java.util.Properties;
import java.util.Random;

/**
 *  kafka 生产者测试代码  Producer
 *
 * @author f_bao
 * @create 2018/5/23
 */
public class TestProducer {

    public static void main(String[] args) {
        long events = 10;
        Random random = new Random();

        Properties properties = new Properties();
        // kafka 集群连接串，集群模式下由多个 host.port 组成
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.189.128:9092,192.168.189.128:9093,192.168.189.128:9094");
        /**
         * broker 消息确认的模式，有三种：（默认 1）
         *  0：不进行消息接受确认，即 Client 端发送完成后不会等待 Broker 的确认
         *  1：由 Leader 确认，Leader 接受到消息后会立即返回确认消息
         *  all: 集群完整确认，Leader会等待所有in-sync的follower节点都确认收到消息后，再返回确认信息
         */
        properties.put(ProducerConfig.ACKS_CONFIG, "all");
        // 发送失败时 Producer 端的重试次数，默认为 0
        properties.put(ProducerConfig.RETRIES_CONFIG, 0);
        // 当同时有大量消息要向同一个分区发送时，Producer 端会将消息打包后进行批量发送，如何设置为 0 ，则每条消息都独立发送，默认为 16384 字节
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        // 发送消息前等待的毫秒数，与 batch.size 配合使用，当消息负载不高的情况下，配置 Linger.ms 能够让 Producer 在发送消息前等待一段时间
        // 已积累更多的消息打包发送，达到节省网络资源的目的
        properties.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        // 消息缓冲池大小，尚未被发送的消息会保持在 Producer 的内存当中，如果消息产生的速度大于消息消费发送的速度，那么缓冲池满了，后面发消息的请求会被阻塞，默认 32MB
        properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        // key.serializer/value.serializer 消息 key/value 的序列化 class,根据 key 和 value 的类型决定
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

        // 配置 producer config 类
        ProducerConfig config = new ProducerConfig(properties);
        // 创建 Producer 生产者
        Producer<String, String> producer = new KafkaProducer<>(properties);

        for (int nEvents = 0; nEvents < events; nEvents++) {
            long runtime = new Date().getTime();
            String ip = "192.168.2." + random.nextInt(255);
            String msg = runtime + ", www.ctrip.com, " + ip;
            ProducerRecord<String, String> data = new ProducerRecord("page_lists", ip, msg);
            producer.send(data, new Callback() {
                @Override
                public void onCompletion(RecordMetadata metadata, Exception e) {
                    if (e != null){
                        e.printStackTrace();
                    }else {
                        System.out.println("The offset of the record we just sent id: " + metadata.offset() );
                    }
                }
            });
        }
        // 关闭生产者
        producer.close();
    }

}