package com.illegalaccess.delay.core.scheduler;

import com.illegalaccess.delay.common.event.DelayEventPublisher;
import com.illegalaccess.delay.core.delay.DelayMessageContainer;
import com.illegalaccess.delay.core.delay.DelayMessageObj;
import com.illegalaccess.delay.message.DelayMqApi;
import com.illegalaccess.delay.toolkit.thread.AbstractLogRunnable;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * loop delay message container to get ready message
 * and dispatch to another thread pool
 *
 * 把延时消息容器内到期的消息投递到mq
 *
 * @date 2021-03-04 10:12
 * @author Jimmy Li
 */
@AllArgsConstructor
@Slf4j
public class LoopDelayMessageThreadPoolTask extends AbstractLogRunnable {

    private ThreadPoolExecutor workingPool;
    private DelayMqApi delayMqApi;
    private DelayEventPublisher delayEventPublisher;

    @Override
    public void runBusiness() {

        DelayMessageObj obj = DelayMessageContainer.poll();
        while (obj != null) {
            workingPool.submit(new ProcessDelayMessageThreadPoolTask(obj, delayMqApi, delayEventPublisher));
            log.info("submit ProcessDelayMessageTask for msg:{}", obj.getMessageId());
            obj = DelayMessageContainer.poll();
        }
    }
}
