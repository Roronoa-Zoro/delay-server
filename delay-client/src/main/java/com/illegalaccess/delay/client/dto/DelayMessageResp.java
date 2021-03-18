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

    private String errorCode;

    private String errorMsg;

    public DelayMessageResp(String messageId) {
        this.messageId = messageId;
    }

    public DelayMessageResp(String messageId, String errorCode, String errorMsg) {
        this.messageId = messageId;
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    @Override
    public String toString() {
        return "DelayMessageResp{" +
                "messageId='" + messageId + '\'' +
                ", errorCode='" + errorCode + '\'' +
                ", errorMsg='" + errorMsg + '\'' +
                '}';
    }
}
