package com.illegalaccess.delay.store.cassandra.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

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
@Table("delay_message_stat")
public class DelayMessageStat implements Serializable {

    private static final long serialVersionUID = 1L;

    @PrimaryKey
    private String appKey;

    /**
     * 消息收到的时间，单位到小时
     */
    private LocalDateTime messageReceivedTime;

    /**
     * 消息收到的数量
     */
    private Integer messageReceivedCnt;


}
