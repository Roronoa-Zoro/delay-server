package com.illegalaccess.delay.core.config;

import com.illegalaccess.delay.core.DelayCoreProperties;
import com.illegalaccess.delay.core.scheduler.ArchiveStoreMessageSchedulerTask;
import com.illegalaccess.delay.store.DelayStoreConstant;
import com.illegalaccess.delay.toolkit.thread.DelayThreadFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 核心模块的配置
 * @author Jimmy Li
 * @date 2021-03-04 10:18
 */
@Configuration
public class DelayCoreConfiguration {

    @Autowired
    private DelayCoreProperties delayCoreProperties;

    /**
     * thread pool for looping delay message which is in memory
     * 轮询内存里面延时消息的线程池
     * @return
     */
    @Bean(value = "loopDelayQueueExecutor", destroyMethod = "shutdown")
    public ScheduledThreadPoolExecutor loopDelayQueueExecutor() {
        ScheduledThreadPoolExecutor pool = new ScheduledThreadPoolExecutor(delayCoreProperties.getLoopThreadSize(),
                new DelayThreadFactory("loop-delay-thread-"),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
        return pool;
    }

    /**
     * thread pool for processing ready delay message
     * 处理到期消息的线程池
     * @return
     */
    @Bean(value = "processDelayQueueExecutor", destroyMethod = "shutdown")
    public ThreadPoolExecutor processDelayQueueExecutor() {
        ThreadPoolExecutor pool = new ThreadPoolExecutor(delayCoreProperties.getWorkingThreadSize(), delayCoreProperties.getWorkingThreadSize(), 180, TimeUnit.SECONDS,
                new SynchronousQueue<>(),
                new DelayThreadFactory("process-delay-thread-"),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
        return pool;
    }

    /**
     * archive completed message task
     * 归档已完成消息的任务
     * @return
     */
    @Bean
    @ConditionalOnProperty(name = DelayStoreConstant.archiveCondition, havingValue = "true", matchIfMissing = true)
    public ArchiveStoreMessageSchedulerTask task() {
        return new ArchiveStoreMessageSchedulerTask();
    }
}
