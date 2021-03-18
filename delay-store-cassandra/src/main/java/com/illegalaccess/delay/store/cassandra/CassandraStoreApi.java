package com.illegalaccess.delay.store.cassandra;

import com.illegalaccess.delay.client.dto.DelayMessageReq;
import com.illegalaccess.delay.store.StoreApi;
import com.illegalaccess.delay.store.cassandra.service.CassandraStoreService;
import com.illegalaccess.delay.store.dto.AppKeyMessageCntDto;
import com.illegalaccess.delay.store.dto.DelayMessageAppDto;
import com.illegalaccess.delay.store.dto.DelayMessageDto;
import com.illegalaccess.delay.store.entity.DelayMessageStat;
import com.illegalaccess.delay.toolkit.dto.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * store api implementation with cassandra
 * @author Jimmy Li
 * @date 2021-03-01 15:03
 */
@Service
public class CassandraStoreApi implements StoreApi {

    @Autowired
    private CassandraStoreService cassandraStoreService;

    @Override
    public String persistDelayMessage(DelayMessageReq req, int slot) {
        return cassandraStoreService.persistDelayMessage(req, slot);
    }

    @Override
    public List<DelayMessageDto> queryExpiringDelayMessage(Long lastId, LocalDateTime startTime, LocalDateTime endTime, List<Integer> slotList) {
        return cassandraStoreService.queryExpiringDelayMessage(lastId, startTime, endTime, slotList);
    }

    @Override
    public List<DelayMessageDto> queryExpiringDelayMessage(LocalDateTime startTime, LocalDateTime endTime, List<Integer> slotList) {
        return cassandraStoreService.queryExpiringDelayMessage(startTime, endTime, slotList);
    }

    @Override
    public boolean cancelDelayMessage(String messageId) {
        return cassandraStoreService.cancelDelayMessage(messageId);
    }

    @Override
    public boolean completeDelayMessage(String messageId) {
        return cassandraStoreService.completeDelayMessage(messageId);
    }

    @Override
    public boolean repeatDelayMessage(String messageId, Long execTime) {
        return cassandraStoreService.repeatDelayMessage(messageId, execTime);
    }

    @Override
    public Long queryTotalMessageCount() {
        return cassandraStoreService.queryTotalMessageCount();
    }

    @Override
    public List<AppKeyMessageCntDto> queryMessageCount4AppKey(List<String> appKeyList) {
        return cassandraStoreService.queryMessageCount4AppKey(appKeyList);
    }

    @Override
    public boolean archiveData(Date now) {
        return cassandraStoreService.archiveData(now);
    }

    @Override
    public DelayMessageAppDto getAppMeta(String appKey) {
        return cassandraStoreService.getAppMeta(appKey);
    }

    @Override
    public int saveDelayMessageSnapshot(List<Pair</*count*/Integer, /*snapshot time*/LocalDateTime>> list, String hostIp) {
        return cassandraStoreService.saveDelayMessageSnapshot(list, hostIp);
    }

    /**
     * 获取所有appkey
     * @return
     */
    @Override
    public List<String> fetchAllAppKey() {
        return cassandraStoreService.fetchAllAppKey();
    }

    /**
     * 存储统计信息
     * @param stats
     * @return
     */
    @Override
    public int saveDelayMessageStat(List<DelayMessageStat> stats) {
        return cassandraStoreService.saveDelayMessageStat(stats);
    }
}
