package com.illegalaccess.delay.core.state;

import com.illegalaccess.delay.client.dto.DelayMessageReq;
import com.illegalaccess.delay.core.business.DelayServerBusiness;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * 服务运行状态
 *
 * @author Jimmy Li
 * @date 2021-02-01 17:38
 */
@Slf4j
@Component("delayServerRunningState")
public class DelayServerRunningState extends AbstractDelayServerState {

    @Autowired
    private DelayServerBusiness delayServerBusiness;

    @Override
    public String acceptMessage(DelayMessageReq req) {
        String messageId = delayServerBusiness.storeDelayMessage(req);
        log.info("message is saved");

        delayServerBusiness.scheduleInMemory(req, messageId);
        return messageId;
    }

    /**
     * 切换到热balance state， 完成正在处理的一条记录，然后执行热balance的逻辑
     */
    @Override
    public void transform() {
        super.getDelayServerContext().setState(super.getDelayServerContext().rebalanceState);
        delayServerBusiness.pauseDispatch();
    }
}
