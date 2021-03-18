package com.illegalaccess.delay.core.state;

import com.illegalaccess.delay.client.dto.DelayMessageReq;
import com.illegalaccess.delay.core.business.DelayServerBusiness;
import com.illegalaccess.delay.protocol.ResourceInfo;
import com.illegalaccess.delay.protocol.ResourceProtocol;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 服务启动状态
 *
 * @author Jimmy Li
 * @date 2021-02-01 17:38
 */
@Slf4j
@Component("delayServerStartupState")
public class DelayServerStartupState extends AbstractDelayServerState {

    @Autowired
    private ResourceProtocol resourceProtocol;
    @Autowired
    private DelayServerBusiness delayServerBusiness;

    private final AtomicBoolean getSlot = new AtomicBoolean(false);

    @Override
    public String service(DelayMessageReq req) {
        if (!getSlot.get()) {
            int[] slot = resourceProtocol.getAllSlot();
            ResourceInfo.assignSlot(slot);
            getSlot.set(true);
        }

        String messageId = delayServerBusiness.storeDelayMessage(req);
        return messageId;
    }

    @Override
    public void transform() {
        super.getDelayServerContext().setState(super.getDelayServerContext().rebalanceState);
    }
}
