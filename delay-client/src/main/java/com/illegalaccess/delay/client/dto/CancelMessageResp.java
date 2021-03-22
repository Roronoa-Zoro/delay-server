package com.illegalaccess.delay.client.dto;

import lombok.Builder;

import java.io.Serializable;

/**
 * 取消消息请求的响应对象
 * @author Jimmy Li
 * @date 2021-03-04 10:14
 */
@Builder
public class CancelMessageResp implements Serializable {

    private static final long serialVersionUID = 904968298751766342L;
    private String messageId;

    public CancelMessageResp(String messageId) {
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
        return "CancelMessageResp{" +
                "messageId='" + messageId + '\'' +
                '}';
    }
}
