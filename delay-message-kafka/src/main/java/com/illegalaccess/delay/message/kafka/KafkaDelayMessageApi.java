package com.illegalaccess.delay.message.kafka;

import com.illegalaccess.delay.message.DelayMessageConstant;
import com.illegalaccess.delay.message.DelayMessageProperties;
import com.illegalaccess.delay.message.DelayMqApi;
import com.illegalaccess.delay.message.SendResultEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Headers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * use kafka to send message
 * kafka实现的消息发送方
 * @date 2021-02-07 12:00
 * @author Jimmy Li
 */
@Slf4j
@Component
public class KafkaDelayMessageApi implements DelayMqApi {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    private DelayMessageProperties delayMessageProperties;

    @Override
    public SendResultEnum sendMessage(String appKey, String topic, String content, String messageId) {
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, content);
        if (delayMessageProperties.isCarryMsgId()) {
            Headers headers = record.headers();
            headers.add(DelayMessageConstant.MSG_ID, messageId.getBytes());
        }

        try {
            ListenableFuture<SendResult<String, String>> sendRes = kafkaTemplate.send(record);
            CompletableFuture<SendResult<String, String>> sendResFuture = sendRes.completable();
            SendResult<String, String> sr = sendResFuture.get();
            sr.getRecordMetadata();
        } catch (InterruptedException | ExecutionException e) {
            log.error("send message:{} to topic:{} fail", messageId, topic, e);
            return SendResultEnum.Failure;
        }

        log.info("send message:{} to topic:{} success", messageId, topic);
        return SendResultEnum.Success;
    }
}
