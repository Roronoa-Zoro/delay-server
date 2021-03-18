package com.illegalaccess.delay.protocol;

import lombok.Data;

import java.io.Serializable;

/**
 * 机器信息
 *
 * @author Jimmy Li
 * @date 2021-01-28 15:49
 */
@Data
public class HostInfo implements Serializable {

    private String hostIp;

    private int port;
}
