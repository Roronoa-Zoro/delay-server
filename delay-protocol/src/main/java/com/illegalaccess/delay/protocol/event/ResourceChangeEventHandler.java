package com.illegalaccess.delay.protocol.event;

/**
 * 1.update working state, switch to re-balance state
 * 2.process complete current data
 * 3.trigger resource re-balance
 * 4.switch back to working state
 *
 * 资源变更处理模板类
 * @author Jimmy Li
 * @date 2021-02-02 17:58
 */
public abstract class ResourceChangeEventHandler {

    /**
     * logic to run before handling resource change event
     */
    protected abstract void beforeHandle();

    /**
     * logic for resource change event
     */
    protected abstract void handle();

    /**
     * logic to run after handling resource change event
     */
    protected abstract void afterHandle();

    /**
     * handle resource change event
     */
    public final void doHandle() {
        beforeHandle();

        handle();

        afterHandle();
    }
}
