package com.illegalaccess.delay.protocol.etcd.task;

import com.illegalaccess.delay.protocol.etcd.support.EtcdResourceInfo;
import com.illegalaccess.delay.protocol.etcd.support.EtcdTool;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 持续续约的任务
 *
 * @author Jimmy Li
 * @date 2021-02-05 14:11
 */
@Slf4j
@AllArgsConstructor
public class LeaseTask implements Runnable {

    private EtcdTool etcdTool;

    @Override
    public void run() {
        log.info("renew a lease:{}", EtcdResourceInfo.getLeaseId());
        etcdTool.renew(EtcdResourceInfo.getLeaseId());
    }
}
