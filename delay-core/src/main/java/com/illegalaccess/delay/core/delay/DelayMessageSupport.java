package com.illegalaccess.delay.core.delay;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 延时消息辅助类
 * @author Jimmy Li
 * @date 2021-03-04 10:36
 */
public class DelayMessageSupport {

    /**
     * 向前查找，最远的一条未处理消息的时间
     */
    public static AtomicReference<LocalDateTime> LastUnProcessedExecTime = new AtomicReference<>();
    /**
     * 向后查找，未处理消息的时间，初始值是可配的，后续每次加载数据，加载未来一分钟的数据
     */
    public static AtomicReference<LocalDateTime> NextUnProcessedExecTime = new AtomicReference<>();

    /**
     * 内部使用的消息topic
     */
    public static final String INNER_DELAY_TOPIC = "delay_inner_topic";

    /**
     * 内部使用的topic对应的appkey
     */
    public static final String INNER_DELAY_APP_KEY = "delay_server_inner_app_key";
}
