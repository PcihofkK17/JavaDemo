package com.example.demo.mq.kafka;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Arrays;
import java.util.Properties;

/**
 * kafka consumer 消费者测试
 *
 *  BUG: kafka Connection timed out: no further information
 *    原因：liunx 启动的防火墙没有打开，打开防火墙即可
 *    centos7:
 *      启动防火墙：firewall-cmd --zone=public --add-port=9092/tcp --permanent （集群下的全部都要打开）
 *      重启防火墙：firewall-cmd --reload
 * @author f_bao
 * @create 2018/5/23
 */
public class TestConsumer {

    public static void main(String[] args) {
        Properties props = new Properties();

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.189.128:9092,192.168.189.128:9093,192.168.189.128:9094");
        props.put(ConsumerConfig.GROUP_ID_CONFIG ,"test") ;
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        Consumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList("page_visits"));

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
            }
        }
    }

}
