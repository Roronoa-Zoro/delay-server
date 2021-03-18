package com.illegalaccess.delay.message;

/**
 * sdk api for different mq client
 *
 * 消息系统的接口
 *
 * @date 2021-02-07 11:58
 * @author Jimmy Li
 */
public interface DelayMqApi {

    /**
     * send message to target mq topic
     * @param appKey
     * @param topic
     * @param content
     * @param messageId
     * @return
     */
    SendResultEnum sendMessage(String appKey, String topic, String content, String messageId);
}
