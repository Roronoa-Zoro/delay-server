package com.illegalaccess.delay.common.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * thread pool executor configuration for spring event
 * common模块的配置
 */
@Configuration
public class DelayCommonAutoConfig {

    private final Logger logger = LoggerFactory.getLogger(DelayCommonAutoConfig.class);

    @Bean("simpleAsyncTaskExecutor4ResourceEventPublisher")
    public SimpleAsyncTaskExecutor simpleAsyncTaskExecutor() {
        return new SimpleAsyncTaskExecutor("delay-event-publisher");
    }

    @Bean("simpleApplicationEventMulticaster4ResourceEventPublisher")
    public SimpleApplicationEventMulticaster applicationEventMulticaster() {
        SimpleApplicationEventMulticaster simpleApplicationEventMulticaster = new SimpleApplicationEventMulticaster();
        simpleApplicationEventMulticaster.setTaskExecutor(simpleAsyncTaskExecutor());
        return simpleApplicationEventMulticaster;
    }

    @Bean(value = "scheduledDelayMessagePublisher", destroyMethod = "destroy")
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(1);
        scheduler.setAwaitTerminationSeconds(30);
        scheduler.setErrorHandler(throwable -> {
            logger.error("scheduledDelayMessagePublisher has error", throwable);
        });

        return scheduler;
    }
}
