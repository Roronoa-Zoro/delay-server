package com.illegalaccess.delay.core.scheduler;

import com.illegalaccess.delay.common.bean.DelayBeanFactory;
import com.illegalaccess.delay.common.event.DelayEventPublisher;
import com.illegalaccess.delay.core.delay.DelayMessageObj;
import com.illegalaccess.delay.core.listener.ReArrangeMessageEvent;
import com.illegalaccess.delay.message.DelayMqApi;
import com.illegalaccess.delay.message.SendResultEnum;
import com.illegalaccess.delay.store.StoreApi;
import com.illegalaccess.delay.toolkit.TimeUtils;
import com.illegalaccess.delay.toolkit.json.JsonTool;
import com.illegalaccess.delay.toolkit.thread.AbstractLogRunnable;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * send delay message to target topic
 * 发送延时消息到目标mq
 * @date 2021-03-04
 * @author Jimmy Li
 */
@Slf4j
@AllArgsConstructor
public class ProcessDelayMessageThreadPoolTask extends AbstractLogRunnable {

    private DelayMessageObj delayMessageObj;
    private DelayMqApi delayMqApi;
    private DelayEventPublisher delayEventPublisher;

    private static RetryConfig config = RetryConfig.<SendResultEnum>custom()
            .maxAttempts(2)
            .retryOnResult(sendResultEnum -> sendResultEnum == SendResultEnum.Failure)
            .retryExceptions(IOException.class, RuntimeException.class)
            .build();
    private static RetryRegistry registry;
    private static Retry retry;
    static {
        // Create a RetryRegistry with a custom configuration
        registry = RetryRegistry.of(config);

        // Get or create a Retry from the registry -
        // Retry will be backed by the default config
        retry = registry.retry("retry4SendMessage");
    }

    // send message to kafka topic,
    // if failure,
    //    retry，
    // if retry failure,
    //    update message time to 20 min later
    @Override
    public void runBusiness() {
        SendResultEnum sendResultEnum = retry.executeSupplier(() ->
            delayMqApi.sendMessage(delayMessageObj.getAppKey(), delayMessageObj.getTopic(), delayMessageObj.getMessage(), delayMessageObj.getMessageId())
        );

        if (SendResultEnum.Failure == sendResultEnum) {
            log.info("send message failure, will update message:{} to a future time", JsonTool.toJsonString(delayMessageObj));
            delayEventPublisher.publishEvent(new ReArrangeMessageEvent(this, delayMessageObj));
        }

        StoreApi storeApi = DelayBeanFactory.getBean(StoreApi.class);
        if (delayMessageObj.getRepeatInterval() > 0) {
            storeApi.repeatDelayMessage(delayMessageObj.getMessageId(), TimeUtils.getTimeStamp() + delayMessageObj.getRepeatInterval());
        } else {
            storeApi.completeDelayMessage(delayMessageObj.getMessageId());
        }

        log.info("send message:{} success", delayMessageObj.getMessageId());
    }
}
