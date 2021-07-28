package com.illegalaccess.delay.core.scheduler;

import com.google.common.primitives.Ints;
import com.illegalaccess.delay.common.bean.DelayBeanFactory;
import com.illegalaccess.delay.core.DelayCoreProperties;
import com.illegalaccess.delay.core.delay.DelayMessageContainer;
import com.illegalaccess.delay.core.delay.DelayMessageObj;
import com.illegalaccess.delay.core.delay.DelayMessageSupport;
import com.illegalaccess.delay.core.transport.DelayCoreConverter;
import com.illegalaccess.delay.protocol.support.ResourceInfo;
import com.illegalaccess.delay.store.StoreApi;
import com.illegalaccess.delay.store.dto.DelayMessageDto;
import com.illegalaccess.delay.toolkit.TimeUtils;
import com.illegalaccess.delay.toolkit.thread.AbstractLogRunnable;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 服务启动时，加载 从30天前 到 当时时间+5分钟 的数据
 * 服务运行中，1）加载 下一分钟的数据， 2）加载从上一次未处理完的数据的执行时间 到 当前时间的数据
 * @date 2021-03-04 10:14
 * @author Jimmy Li
 */
@Slf4j
@AllArgsConstructor
public class LoadDelayMessageThreadPoolTask extends AbstractLogRunnable {

    private DelayCoreProperties delayCoreProperties;

    @Override
    public void runBusiness() {
        int[] assignedSlot = ResourceInfo.getAssignedSlot();
        if (assignedSlot == null) {
            log.info("assigned slot is not ready");
            return;
        }
        StoreApi storeApi = DelayBeanFactory.getBean(StoreApi.class);
        DelayCoreConverter delayCoreConverter = DelayBeanFactory.getBean(DelayCoreConverter.class);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime lastExecTime = DelayMessageSupport.LastUnProcessedExecTime.get();
        // server start up logic
        if (lastExecTime == null) {

            LocalDateTime future5Min = now.plusMinutes(delayCoreProperties.getWithinMin());
            DelayMessageSupport.NextUnProcessedExecTime.set(future5Min);

            LocalDateTime startTime = TimeUtils.addTime(-30, TimeUnit.DAYS);
            List<DelayMessageDto> list = storeApi.queryExpiringDelayMessage(startTime, future5Min, Ints.asList(assignedSlot));
            if (CollectionUtils.isEmpty(list)) {
                log.info("there is no expiring message");
                DelayMessageSupport.LastUnProcessedExecTime.set(now);
                return;
            }

            LocalDateTime last = putDelayMessageToMemory(list, delayCoreConverter);
            setLastUnProcessedTime(last);

            log.info("load delay message for server start up, between {} and {}", startTime, future5Min);
            return;
        }

        // running logic
        /*
           维护一个时间窗口
           last     next
           |         |
           ___________
           1。每次向后查找时，范围是next - next+60s
           2。每次向前查找时，从last到当前时间范围，如果每次没有失败的记录，则每次向前查找的就是刚过去的一分钟内de数据
           向前查找是为了避免之前的数据处理失败，这样可以进行重试
         */
        LocalDateTime lastFutureTime = DelayMessageSupport.NextUnProcessedExecTime.get();
        LocalDateTime nextFutureTime = lastFutureTime.plusSeconds(60);
        List<DelayMessageDto> list = storeApi.queryExpiringDelayMessage(lastFutureTime, nextFutureTime, Ints.asList(ResourceInfo.getAssignedSlot()));
        DelayMessageSupport.NextUnProcessedExecTime.set(nextFutureTime);
        if (!CollectionUtils.isEmpty(list)) {
            List<DelayMessageObj> dataList = delayCoreConverter.toDelayMessageObjList(list);
            dataList.forEach(data -> DelayMessageContainer.add(data));
        }
        log.info("load delay message for next 1 min between {} and {}", lastFutureTime, nextFutureTime);

        list = storeApi.queryExpiringDelayMessage(lastExecTime, now, Ints.asList(ResourceInfo.getAssignedSlot()));
        log.info("load unprocessed delay message for previous time between {} and {}", lastExecTime, now);
        if (CollectionUtils.isEmpty(list)) {
            DelayMessageSupport.LastUnProcessedExecTime.set(now);
            return;
        }

        LocalDateTime last = putDelayMessageToMemory(list, delayCoreConverter);
        setLastUnProcessedTime(last);
    }

    /**
     * 设置向前查找的最大时间，最长向前推10分钟，因为随着一次一次的重复执行，每条失败的记录会被重复执行10次
     * @param lastRecordTime
     */
    private void setLastUnProcessedTime(LocalDateTime lastRecordTime) {
        LocalDateTime tenMinAgo = LocalDateTime.now().plusMinutes(10);
        DelayMessageSupport.LastUnProcessedExecTime.set(lastRecordTime.isAfter(tenMinAgo) ? lastRecordTime : tenMinAgo);
    }

    /**
     * 把集合里面时间最早的那条记录的执行时间付给这个字段
     */
    private LocalDateTime putDelayMessageToMemory(List<DelayMessageDto> list, DelayCoreConverter delayCoreConverter) {
        List<DelayMessageObj> dataList = delayCoreConverter.toDelayMessageObjList(list);
        LocalDateTime min = LocalDateTime.now().plusHours(1);

        for (DelayMessageObj delayMessageObj : dataList) {
            LocalDateTime tmp = TimeUtils.toLocalDateTime(delayMessageObj.getTtl());
            if (tmp.isBefore(min)) {
                min = tmp;
            }
            DelayMessageContainer.add(delayMessageObj);
        }

        return min;
    }
}
