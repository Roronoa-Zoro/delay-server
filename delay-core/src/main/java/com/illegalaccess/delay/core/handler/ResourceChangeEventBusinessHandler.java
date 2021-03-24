package com.illegalaccess.delay.core.handler;

import com.illegalaccess.delay.core.state.DelayServerContext;
import com.illegalaccess.delay.protocol.event.ResourceChangeEventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * before handle logic:
 * 1.update working state, switch to re-balance state
 * 2.process complete current data
 * <p>
 * after handle logic:
 * 1.switch back to working state
 *
 * @author Jimmy Li
 * @date 2021-02-02 17:58
 */
@Component
public class ResourceChangeEventBusinessHandler extends ResourceChangeEventHandler {

    @Autowired
    private DelayServerContext delayServerContext;

    @Override
    protected void beforeHandle() {
        delayServerContext.pause();
    }

    @Override
    protected void afterHandle() {
        delayServerContext.run();
    }
}
