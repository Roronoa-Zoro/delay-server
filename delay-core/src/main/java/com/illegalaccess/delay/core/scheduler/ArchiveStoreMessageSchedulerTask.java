package com.illegalaccess.delay.core.scheduler;

import com.illegalaccess.delay.store.StoreApi;
import com.illegalaccess.delay.toolkit.scheduler.SchedulerTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;

/**
 * archive completed message task
 * 把已经处理完成的消息进行归档的任务
 * @date 2021-03-04 10:14
 * @author Jimmy Li
 */
@Slf4j
public class ArchiveStoreMessageSchedulerTask implements SchedulerTask {

    @Autowired
    private StoreApi storeApi;

    @Override
    @Scheduled(cron = "${delay.store.archiveScheduler}")
    public void doBusiness() {
        log.info("ArchiveStoreMessageScheduler begin");
        storeApi.archiveData(new Date());
        log.info("ArchiveStoreMessageScheduler end");
    }
}
