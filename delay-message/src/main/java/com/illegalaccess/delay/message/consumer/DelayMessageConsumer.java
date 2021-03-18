package com.illegalaccess.delay.message.consumer;

/**
 * 消息消费处理器
 */
public interface DelayMessageConsumer {

    boolean processMessage(String msg);
}
