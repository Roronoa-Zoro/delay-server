package com.illegalaccess.delay.protocol.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;

/**
 * monitor resource change event, then invoke state change
 * 资源事件变更监听器，触发状态切换
 * @author Jimmy Li
 * @date 2021-02-02 14:52
 */
public class ResourceChangeListener implements ApplicationListener<ResourceChangeEvent> {

    private final Logger logger = LoggerFactory.getLogger(ResourceChangeListener.class);

    @Autowired
    private ResourceChangeEventHandler resourceChangeEventHandler;

    /**
     * 1.update working state, switch to re-balance state
     * 2.process complete current data
     * 3.trigger resource re-balance
     * 4.switch back to working state
     *
     * @param resourceChangeEvent
     */
    @Override
    public void onApplicationEvent(ResourceChangeEvent resourceChangeEvent) {
        logger.info("trigger resource change logic for:{}", resourceChangeEvent);
        resourceChangeEventHandler.doHandle();
    }
}
