package com.lijs.rank.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.kafka.annotation.KafkaListener;

/**
 * @author ljs
 * @date 2024-12-30
 * @description
 */
public class KafkaListenerDemo {


    @KafkaListener(topics = "test.topic", groupId = "group100", containerFactory = "batchKafkaListenerContainerFactory")
    public void resLoginLogListener(ConsumerRecords<String, Object> records) {
        // 自定义批量订阅
    }

}
