package com.illegalaccess.delay.protocol.support;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

/**
 * 机器信息
 *
 * @author Jimmy Li
 * @date 2021-01-28 15:49
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HostInfo implements Serializable {

    private String hostIp;

    private int port;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HostInfo)) {
            return false;
        }
        HostInfo hostInfo = (HostInfo) o;
        return hostIp.equals(hostInfo.hostIp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hostIp);
    }
}
