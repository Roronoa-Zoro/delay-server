package com.illegalaccess.delay.toolkit.scheduler;

/**
 * identify sub class is spring managed scheduled task
 * @date 2021-03-04 10:34
 * @author Jimmy Li
 */
public interface SchedulerTask {

    /**
     * 业务逻辑
     */
    void doBusiness();
}
