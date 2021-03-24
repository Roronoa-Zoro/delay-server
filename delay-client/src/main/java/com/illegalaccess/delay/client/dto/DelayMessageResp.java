package com.illegalaccess.delay.client.dto;

import java.io.Serializable;

/**
 * 发送延续消息的响应对象
 * @author Jimmy Li
 * @date 2021-03-04 10:14
 */
public class DelayMessageResp implements Serializable {

    private static final long serialVersionUID = 3586598000348789289L;
    private String messageId;

    public DelayMessageResp(String messageId) {
        this.messageId = messageId;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    @Override
    public String toString() {
        return "DelayMessageResp{" +
                "messageId='" + messageId + '\'' +
                '}';
    }
}
