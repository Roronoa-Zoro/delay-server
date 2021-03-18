package com.illegalaccess.delay.store.dto;

import lombok.Data;

import java.io.Serializable;

/**
 *
 * @author Jimmy Li
 * @date 2021-03-04 10:29
 */
@Data
public class DelayMessageDto implements Serializable {

    private String appKey;
    private String topic;

    private Long execTime;
    private String msgContent;
    private String msgId;
    private Long repeatInterval;
}
