package com.illegalaccess.delay.common.event;

import com.illegalaccess.delay.toolkit.thread.DelayThreadFactory;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 资源状态变更发布
 *
 * @author Jimmy Li
 * 在上层依赖里实现异步逻辑
 * @date 2021-02-02 14:31
 */
@Component
public class DelayEventPublisher {

    private final Logger logger = LoggerFactory.getLogger(DelayEventPublisher.class);

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    private DelayQueue<DelayMessageEvent> delayQueue = new DelayQueue<>();
    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1,
            new DelayThreadFactory("delay-publisher-thread-"),
            new ThreadPoolExecutor.CallerRunsPolicy());

    @PostConstruct
    public void init() {
        scheduledThreadPoolExecutor.scheduleAtFixedRate(
                new DelayMessageEventPublishTask(delayQueue, applicationEventPublisher),
                100L,
                100L,
                TimeUnit.MILLISECONDS
        );
    }

    @PreDestroy
    public void destroy() {
        logger.info("shutdown delay-publisher-thread");
        scheduledThreadPoolExecutor.shutdown();
    }
    /**
     * 发布资源变更事件
     *
     * @param event
     */
    public <T extends ApplicationEvent> void publishEvent(T event) {
        logger.info("publish {}", event);
        applicationEventPublisher.publishEvent(event);
    }

    /**
     *
     * @param event
     * @param delayMs
     * @param <T>
     */
    public <T extends ApplicationEvent> void publishEventWithDelay(T event, Long delayMs) {
        logger.info("publish:{} with delay:{}", event, delayMs);
        delayQueue.clear();
        delayQueue.add(DelayMessageEvent.builder().applicationEvent(event).delayMs(delayMs).build());
    }

    @AllArgsConstructor
    class DelayMessageEventPublishTask implements Runnable {
        DelayQueue<DelayMessageEvent> delayQueue;
        private ApplicationEventPublisher applicationEventPublisher;

        @Override
        public void run() {
            DelayMessageEvent delayMessageEvent = delayQueue.poll();
            while (delayMessageEvent != null) {
                logger.info("publish delay event:{}", delayMessageEvent);
                applicationEventPublisher.publishEvent(delayMessageEvent.getApplicationEvent());
                delayMessageEvent = delayQueue.poll();
            }
        }
    }
}
