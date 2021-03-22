package com.illegalaccess.delay.core.state;

import com.illegalaccess.delay.client.dto.DelayMessageReq;
import com.illegalaccess.delay.core.business.DelayServerBusiness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 重新平衡状态
 *
 * @author Jimmy Li
 * @date 2021-02-01 11:01
 */
@Component("delayServerRebalancingState")
public class DelayServerRebalancingState extends AbstractDelayServerState {

    @Autowired
    private DelayServerBusiness delayServerBusiness;

    @Override
    public String acceptMessage(DelayMessageReq req) {
        // 接收数据，直接入存储层
        String messageId = delayServerBusiness.storeDelayMessage(req);
        return messageId;
    }

    @Override
    public void transform() {
        super.getDelayServerContext().setState(super.getDelayServerContext().runningState);
    }
}
