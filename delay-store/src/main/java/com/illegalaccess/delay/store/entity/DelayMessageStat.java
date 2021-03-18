package com.illegalaccess.delay.store.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 延时消息统计表
 * </p>
 *
 * @author Jimmy Li
 * @since 2021-02-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DelayMessageStat implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * pk
     */
    private Long id;

    private String appKey;

    private String topic;

    /**
     * 消息收到的时间，单位到小时
     */
    private LocalDateTime messageReceivedTime;

    /**
     * 消息收到的数量
     */
    private Integer messageReceivedCnt;


}
