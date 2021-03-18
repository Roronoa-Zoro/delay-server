package com.illegalaccess.delay.store;

/**
 * 存储模块的常量类
 * @author Jimmy Li
 * @date 2021-03-04 10:29
 */
public interface DelayStoreConstant {

    /**
     *
     */
    String archiveCondition = "delay.store.archive";

    /**
     * cron for archiving message
     */
    String archiveScheduler = "delay.store.archiveScheduler";
}
