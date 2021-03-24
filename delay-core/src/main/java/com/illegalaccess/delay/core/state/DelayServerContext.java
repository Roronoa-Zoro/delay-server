package com.illegalaccess.delay.core.state;

import com.illegalaccess.delay.client.dto.CancelMessageReq;
import com.illegalaccess.delay.client.dto.DelayMessageReq;
import com.illegalaccess.delay.core.DelayCoreProperties;
import com.illegalaccess.delay.store.StoreApi;
import com.illegalaccess.delay.store.dto.DelayMessageAppDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.concurrent.atomic.AtomicInteger;

/**
 状态切换过程
 启动状态
   |
   |
   |
   ↓
 重平衡状态<------------->工作状态

 * @date 2021-03-04 10:20
 * @author Jimmy Li

 */
@Component
public class DelayServerContext {

    private final Logger logger = LoggerFactory.getLogger(DelayServerContext.class);

    private volatile AbstractDelayServerState delayServerState;

    @Resource(name = "delayServerRunningState")
    AbstractDelayServerState runningState;

    @Resource(name = "delayServerRebalancingState")
    AbstractDelayServerState rebalanceState;

    @Resource(name = "delayServerStartupState")
    AbstractDelayServerState startupState;

    private final int rebalance_state = 1;
    private final int non_rebalance_state = 0;
    private final AtomicInteger state = new AtomicInteger(non_rebalance_state);

    @Autowired
    private StoreApi storeApi;

    @Autowired
    private DelayCoreProperties delayCoreProperties;

    @PostConstruct
    public void init() {
        startupState.setDelayServerContext(this);
        runningState.setDelayServerContext(this);
        rebalanceState.setDelayServerContext(this);
        delayServerState = startupState;
    }

    public void setState(AbstractDelayServerState delayServerState) {
        this.delayServerState = delayServerState;
    }

    public String acceptMessage(DelayMessageReq req) {
        req.setExecTime(req.getExecTime() - delayCoreProperties.getNetworkDelay());
        DelayMessageAppDto appMetaData = storeApi.getAppMeta(req.getAppKey());
        if (appMetaData == null) {
            throw new IllegalArgumentException("invalid appKey:" + req.getAppKey());
        }

        return delayServerState.acceptMessage(req);
    }

    /**
     * 取消延时消息
     * @param req
     * @return
     */
    public String cancelMessage(CancelMessageReq req) {
        return delayServerState.cancelMessage(req);
    }

    /**
     * switch re-balance state
     */
    public void pause() {
        if (rebalance_state == state.get()) {
            logger.info("already in rebalance state, can not transform");
            return;
        }

        if (state.compareAndSet(non_rebalance_state, rebalance_state)) {
            logger.info("to re-balance state");
            delayServerState.transform();
        }
    }

    /**
     * recover back to working state
     */
    public void run() {
        if (state.compareAndSet(rebalance_state, non_rebalance_state)) {
            logger.info("back to working state");
            delayServerState.transform();
        }
    }
}
