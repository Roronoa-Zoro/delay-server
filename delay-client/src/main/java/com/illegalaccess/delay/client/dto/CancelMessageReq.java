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
}
