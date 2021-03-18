package com.illegalaccess.delay.store.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 服务器内存里面的，消息数量的快照信息
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DelayMessageMemorySnapshot implements Serializable {

    /**
     * pk
     */
    private Long id;

    /**
     * 服务器IP
     */
    private String hostIp;

    /**
     * 消息数量快照
     */
    private Integer messageCnt;

    /**
     * 快照时间
     */
    private LocalDateTime snapshotTime;
}
