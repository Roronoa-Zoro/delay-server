package com.illegalaccess.delay.core.state;

import com.illegalaccess.delay.client.dto.CancelMessageReq;
import com.illegalaccess.delay.client.dto.DelayMessageReq;
import com.illegalaccess.delay.common.bean.DelayBeanFactory;
import com.illegalaccess.delay.core.business.DelayServerBusiness;

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
    public abstract String acceptMessage(DelayMessageReq req);

    /**
     * 取消消息
     * @param req
     * @return
     */
    public String cancelMessage(CancelMessageReq req) {
        DelayServerBusiness delayServerBusiness = DelayBeanFactory.getBean(DelayServerBusiness.class);
        return delayServerBusiness.cancelMessage(req);
    }

    /**
     * 状态转换
     */
    public abstract void transform();
}
