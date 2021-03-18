package com.illegalaccess.delay.core.business;

import com.illegalaccess.delay.client.dto.DelayMessageReq;
import com.illegalaccess.delay.core.delay.DelayMessageContainer;
import com.illegalaccess.delay.core.delay.DelayMessageObj;
import com.illegalaccess.delay.core.delay.DelayMessageStatSupport;
import com.illegalaccess.delay.core.delay.DelayMessageSupport;
import com.illegalaccess.delay.core.transport.DelayCoreConverter;
import com.illegalaccess.delay.protocol.ResourceInfo;
import com.illegalaccess.delay.store.StoreApi;
import com.illegalaccess.delay.toolkit.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 延时消息业务操作
 * @date 2021-03-04 10:18
 * @author Jimmy Li
 */
@Slf4j
@Component
public class DelayServerBusiness {

    @Autowired
    private StoreApi storeApi;
    @Autowired
    private DelayCoreConverter delayCoreConverter;


    /**
     * save delay message to store layer
     * @param req
     * @return unique message id
     */
    public String storeDelayMessage(DelayMessageReq req) {
        String messageId = storeApi.persistDelayMessage(req, ResourceInfo.getSlot());
        DelayMessageStatSupport.increaseTopicCnt(req.getAppKey(), req.getTopic());
        return messageId;
    }

    /**
     * store delay message in memory when its time within specified value
     */
    public void scheduleInMemory(DelayMessageReq req, String messageId) {
//        LocalDateTime future = TimeUtils.addTime(req.getDelay(), req.getTimeUnit());
        LocalDateTime future = TimeUtils.toLocalDateTime(req.getExecTime());
        /*
           只有执行时间<=nextUnProcessedExecTime的消息，才投递到内存，
           不使用执行时间<=now + delay.core.whthin的原因是，有可能执行时间的范围在nextUnProcessedExecTime 和 now + delay.core.whthin之间，即下一次load数据的时间区间内，这样会造成从数据库中加载很多已经放入到内存的数据
         */
        if (future.isAfter(DelayMessageSupport.NextUnProcessedExecTime.get())) {
            log.debug("msg:{} will be loaded later", messageId);
            return;
        }

//        long ttl = TimeUtils.toTimeStamp(future);
        DelayMessageObj obj = delayCoreConverter.toDelayMessageObj(req, messageId);
        DelayMessageContainer.add(obj);
        log.info("message:{} is put into memory", messageId);
    }

    /**
     *
     */
    public void pauseDispatch() {
        log.info("parse dispatch");
        DelayMessageContainer.clear();
    }
}
