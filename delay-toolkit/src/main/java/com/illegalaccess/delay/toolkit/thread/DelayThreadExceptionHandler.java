package com.illegalaccess.delay.toolkit.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 线程池未捕获异常处理器
 *
 * @author Jimmy Li
 * @date 2021-02-01 10:17
 */
public class DelayThreadExceptionHandler implements Thread.UncaughtExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(DelayThreadExceptionHandler.class);

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        logger.error("thread:{} throw exception", t.getName(), e);
    }
}
