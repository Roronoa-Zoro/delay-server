package com.illegalaccess.delay.core.message;

import com.illegalaccess.delay.cache.DelayCacheKey;
import com.illegalaccess.delay.cache.DelayCacheSupport;
import com.illegalaccess.delay.message.consumer.DelayMessageConsumer;
import com.illegalaccess.delay.store.StoreApi;
import com.illegalaccess.delay.store.entity.DelayMessageStat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 把缓存里面appkey+topic维度的消息数量，持久化到存储, 并再次写入一条延时消息
 */
@Slf4j
@Component
public class DelayMessageInnerConsumer implements DelayMessageConsumer {

    @Autowired
    private DelayCacheKey delayCacheKey;

    @Autowired
    private DelayCacheSupport delayCacheSupport;

    @Autowired
    private StoreApi storeApi;

    private final int batch = 1000;

    /**
     * 1.获取所有的appkey list
     * 2.遍历缓存，取出所有数据，批量入库
     * 3.写入一条新的延时消息
     * @param msg
     * @return
     */
    @Override
    public boolean processMessage(String msg) {
        List<String> appKeyList = storeApi.fetchAllAppKey();
        if (CollectionUtils.isEmpty(appKeyList)) {
            log.info("appkey list is empty");
            return true;
        }

        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        List<DelayMessageStat> stats = new ArrayList<>(batch);
        appKeyList.forEach(appKey -> {
            String hashKey = delayCacheKey.createAppKeyAndTopicCntKey(appKey);
            Map</*topic*/Object, /*count*/Object> entries = delayCacheSupport.entries(hashKey);

            entries.forEach((k,v) -> {
                DelayMessageStat stat = new DelayMessageStat();
                stat.setMessageReceivedTime(now);
                int cnt = Integer.parseInt(v.toString());
                if (cnt == 0) {
                    return;
                }
                stat.setMessageReceivedCnt(cnt);
                stat.setAppKey(appKey);
                stat.setTopic(k.toString());
                delayCacheSupport.incrHashField(hashKey, k.toString(), -cnt);
                stats.add(stat);
                if (stats.size() % batch == 0) {
                    storeApi.saveDelayMessageStat(stats);
                    stats.clear();
                }
            });
            log.info("msg count under topic for appKey:{} done", appKey);
        });

        storeApi.saveDelayMessageStat(stats);
        return true;
    }
}
