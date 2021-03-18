package com.illegalaccess.delay.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

/**
 * thread pool executor configuration for spring event
 * common模块的配置
 */
@Configuration
public class DelayCommonAutoConfig {

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
}
