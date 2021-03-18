package com.illegalaccess.delay.core.state;

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

    public String doService(DelayMessageReq req) {
        req.setExecTime(req.getExecTime() - delayCoreProperties.getNetworkDelay());
        DelayMessageAppDto appMetaData = storeApi.getAppMeta(req.getAppKey());
        if (appMetaData == null) {
            throw new IllegalArgumentException("invalid appKey:" + req.getAppKey());
        }

        return delayServerState.service(req);
    }

    /**
     * switch re-balance state
     */
    public void pause() {
        logger.info("to re-balance state");
        delayServerState.transform();
    }

    /**
     * recover back to working state
     */
    public void run() {
        logger.info("back to working state");
        delayServerState.transform();
    }
}
