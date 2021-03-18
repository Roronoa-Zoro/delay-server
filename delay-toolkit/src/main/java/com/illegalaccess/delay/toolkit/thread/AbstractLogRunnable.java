package com.illegalaccess.delay.toolkit.thread;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 带异常处理的runnable
 * @author Jimmy Li
 * @date 2021-03-04 10:34
 */
public abstract class AbstractLogRunnable implements Runnable {

    private Logger logger = LoggerFactory.getLogger(AbstractLogRunnable.class);

    @Override
    public void run() {
        try {
            runBusiness();
        } catch (Exception e) {
            logger.error("AbstractLogRunnable get exception", e);
        }
    }

    /**
     * business logic
     */
    public abstract void runBusiness();
}
