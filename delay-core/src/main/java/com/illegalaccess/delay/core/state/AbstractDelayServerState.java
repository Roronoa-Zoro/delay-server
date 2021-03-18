package com.illegalaccess.delay.core.state;

import com.illegalaccess.delay.client.dto.DelayMessageReq;

/**
 * 抽象状态类
 * @author Jimmy Li
 * @date 2021-03-04 10:18
 */
public abstract class AbstractDelayServerState {

    private DelayServerContext delayServerContext;

    public DelayServerContext getDelayServerContext() {
        return delayServerContext;
    }

    protected void setDelayServerContext(DelayServerContext delayServerContext) {
        this.delayServerContext = delayServerContext;
    }

    /**
     * 提供服务
     * return messageId
     */
    public abstract String service(DelayMessageReq req);

    /**
     * 状态转换
     */
    public abstract void transform();
}
