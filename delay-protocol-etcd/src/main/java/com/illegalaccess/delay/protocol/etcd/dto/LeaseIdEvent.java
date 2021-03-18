package com.illegalaccess.delay.protocol.etcd.dto;

import lombok.Data;

import java.io.Serializable;

/**
 *
 * @author Jimmy Li
 * @date 2021-03-04 10:28
 */
@Data
public class LeaseIdEvent implements Serializable {

    /**
     * 续约id
     */
    private Long leaseId;

    /**
     * 每隔多少秒进行一次续约
     */
    private Integer period;

    /**
     * 上次进行续约的时间
     */
    private Long lastLeaseTime;
}
