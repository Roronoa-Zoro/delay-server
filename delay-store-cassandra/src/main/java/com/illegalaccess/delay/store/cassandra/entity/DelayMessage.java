package com.illegalaccess.delay.store.cassandra.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

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
@Table("delay_message")
public class DelayMessage implements Serializable {

    private static final long serialVersionUID = 1L;

//    @PrimaryKey
//    private Long id;
    /**
     * 消息id，唯一
     */
    @PrimaryKeyColumn(value = "msg_id", ordinal = 1, type = PrimaryKeyType.PARTITIONED)
//    @Column("msg_id")
    private String msgId;

    /**
     * 执行时间
     */
    @Column("exec_time")
    private Long execTime;

    /**
     * 消息内容
     */
    @Column("msg_content")
    private String msgContent;

    /**
     * mq topic
     */
    @Column("topic")
    private String topic;

    /**
     * 1=有效
     */
    @Column("status")
    private Integer status;

    /**
     * 更新时间
     */
    @Column("modify_time")
    private LocalDateTime modifyTime;

    @Column("app_key")
    private String appKey;

    /**
     * 所属的槽
     */
//    @Column("slot")
    @PrimaryKeyColumn(value = "slot", ordinal = 2)
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
