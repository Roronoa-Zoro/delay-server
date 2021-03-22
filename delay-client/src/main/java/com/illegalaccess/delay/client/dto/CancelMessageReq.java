package com.illegalaccess.delay.client.dto;

import java.io.Serializable;

/**
 * 取消消息的入参对象
 * @author Jimmy Li
 * @date 2021-03-04 10:14
 */

public class CancelMessageReq implements Serializable {

    private static final long serialVersionUID = -1106109566323207330L;
    private String appKey;

    private String topic;

    private String messageId;

    public CancelMessageReq(String appKey, String topic, String messageId) {
        this.appKey = appKey;
        this.topic = topic;
        this.messageId = messageId;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    @Override
    public String toString() {
        return "CancelMessageReq{" +
                "appKey='" + appKey + '\'' +
                ", topic='" + topic + '\'' +
                ", messageId='" + messageId + '\'' +
                '}';
    }
}
