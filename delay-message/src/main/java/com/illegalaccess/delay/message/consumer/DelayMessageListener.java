package com.illegalaccess.delay.message.consumer;

/**
 * 延时消息监听器
 */
public interface DelayMessageListener {

    boolean onMessage(String msg);
}
