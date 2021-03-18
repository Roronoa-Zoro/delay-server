package com.illegalaccess.delay.core.delay;

import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;
import java.util.concurrent.DelayQueue;

/**
 * delay message container in memory
 * 延时消息内存容器
 * @date 2021-03-04 10:18
 * @author Jimmy Li
 */
@Slf4j
public class DelayMessageContainer {

    private static DelayQueue<DelayMessageObj> delayMessageQueue = new DelayQueue<>();
    private static Set<DelayMessageObj> filter = Sets.newConcurrentHashSet();

    /**
     * add delay message
     * @param obj
     * @return
     */
    public static final boolean add(DelayMessageObj obj) {
        if (filter.add(obj)) {
            return delayMessageQueue.add(obj);
        }
        log.info("delay message:{} is already in memory", obj.getMessageId());
        return false;
    }

    /**
     * get ready delay message
     * @return
     */
    public static final DelayMessageObj poll() {
        DelayMessageObj data = delayMessageQueue.poll();
        if (data != null) {
            filter.remove(data);
        }

        return data;
    }

    /**
     *
     */
    public static final void clear() {
        delayMessageQueue.clear();
        filter.clear();
    }

    /**
     * cancel a message
     * @param messageId
     * @return
     */
    public static final boolean cancelMsg(String messageId) {
        DelayMessageObj obj = new DelayMessageObj();
        obj.setMessageId(messageId);

        boolean removed = delayMessageQueue.remove(obj);

        filter.remove(obj);

        log.info("cancel msg:{} result:{}", messageId, removed);
        return removed;
    }

    /**
     * count current memory message size
     * @return
     */
    public static final int countMsg() {
        return filter.size();
    }
}
