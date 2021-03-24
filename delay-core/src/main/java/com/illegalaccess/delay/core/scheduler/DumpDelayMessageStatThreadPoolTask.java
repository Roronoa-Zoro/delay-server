package com.illegalaccess.delay.core.scheduler;


import com.illegalaccess.delay.cache.DelayCacheKey;
import com.illegalaccess.delay.cache.DelayCacheSupport;
import com.illegalaccess.delay.common.bean.DelayBeanFactory;
import com.illegalaccess.delay.core.delay.DelayMessageStatSupport;
import com.illegalaccess.delay.protocol.support.ResourceInfo;
import com.illegalaccess.delay.store.StoreApi;
import com.illegalaccess.delay.toolkit.dto.Pair;
import com.illegalaccess.delay.toolkit.thread.AbstractLogRunnable;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 统计内存相关信息
 * 1.消息总量
 * 2.汇总存储App+topic维度的消息数量
 * 3.汇总存储内存里面的待处理的延时消息的数量
 */
@Slf4j
public class DumpDelayMessageStatThreadPoolTask extends AbstractLogRunnable {

    private final int snapshotThreahold = 5;

    @Override
    public void runBusiness() {
        /*
            消息总量直接记录到缓存
         */
        int totalMsgCnt = DelayMessageStatSupport.dumpTotalMessageCnt();
        DelayCacheSupport delayCacheSupport = DelayBeanFactory.getBean(DelayCacheSupport.class);
        DelayCacheKey delayCacheKey = DelayBeanFactory.getBean(DelayCacheKey.class);
        String totalMsgCntCacheKey = delayCacheKey.createTotalMsgCntKey();

        delayCacheSupport.incr(totalMsgCntCacheKey, totalMsgCnt);
        log.info("total msg increased:{}", totalMsgCnt);

        Map<Pair</*appkey*/String, /*topic*/String>, /*count*/Integer> data = DelayMessageStatSupport.dumpTopicCnt();

        // 批量写入, 更新缓存，然后依赖延时消息写入数据库
        if (!data.isEmpty()) {
            data.forEach((k,v) -> {

                String appKeyHash = delayCacheKey.createAppKeyAndTopicCntKey(k.getFirst());
                delayCacheSupport.incrHashField(appKeyHash, k.getSecond(), v);
            });
            log.info("msg count for appKey + topic processed");
        }


        int snapshotSize = DelayMessageStatSupport.memorySnapshot();
        if (snapshotSize >= snapshotThreahold) {
            StoreApi storeApi = DelayBeanFactory.getBean(StoreApi.class);
            // 存储, 批量写入
            List<Pair<Integer, LocalDateTime>> list = DelayMessageStatSupport.dumpMemorySnapshot();
            storeApi.saveDelayMessageSnapshot(list, ResourceInfo.getSelf().getHostIp());
            log.info("snapshot list is saved");
        }
    }
}
