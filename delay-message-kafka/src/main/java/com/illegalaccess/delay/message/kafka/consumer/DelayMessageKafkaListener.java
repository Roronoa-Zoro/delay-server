package com.illegalaccess.delay.message.kafka.consumer;

import com.illegalaccess.delay.message.consumer.DelayMessageConsumer;
import com.illegalaccess.delay.message.consumer.DelayMessageListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;

/**
 * kafka实现的监听内部使用的topic
 */
@Slf4j
@EnableKafka
@Configuration
public class DelayMessageKafkaListener implements DelayMessageListener {

    @Autowired
    private DelayMessageConsumer delayMessageConsumer;


    @Override
    public boolean onMessage(String msg) {
        return delayMessageConsumer.processMessage(msg);
    }

    @KafkaListener(topics = "delay_inner_topic", concurrency = "1")
    public void onKafkaMessage(String msg, Acknowledgment acknowledgment) {
        boolean success = onMessage(msg);
        if (success) {
            acknowledgment.acknowledge();
        }
    }
}
