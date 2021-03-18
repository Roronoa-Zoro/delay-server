package com.illegalaccess.delay.store.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 延时消息表
 * </p>
 *
 * @author Jimmy Li
 * @since 2021-02-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DelayMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    /**
     * 消息id，唯一
     */
    private String msgId;

    /**
     * 执行时间
     */
    private Long execTime;

    /**
     * 消息内容
     */
    private String msgContent;

    /**
     * mq topic
     */
    private String topic;

    /**
     * 1=有效
     */
    private Integer status;

    /**
     * 更新时间
     */
    private LocalDateTime modifyTime;

    private String appKey;

    /**
     * 所属的槽
     */
    private Integer slot;

    /**
     * 循环类型 0=一次性任务
     * 1=循环消息
     */
    private Integer repeatType;

    /**
     * 循环消息时下次消息的延时时间，即当前时间+该字段，单位ms
     */
    private Integer repeatInterval;

}
