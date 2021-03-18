package com.illegalaccess.delay.protocol.etcd;

import com.illegalaccess.delay.protocol.ResourceInfo;

/**
 *
 * @author Jimmy Li
 * @date 2021-03-04 10:29
 */
public class EtcdResourceInfo extends ResourceInfo {

    private static Long leaseId;

    public static void updateLease(Long leaseId) {
        EtcdResourceInfo.leaseId = leaseId;
    }

    public static Long getLeaseId() {
        return EtcdResourceInfo.leaseId;
    }
}
