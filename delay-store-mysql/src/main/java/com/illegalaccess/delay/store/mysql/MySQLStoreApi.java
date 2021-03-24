package com.illegalaccess.delay.store.mysql;


import com.illegalaccess.delay.client.dto.DelayMessageReq;
import com.illegalaccess.delay.store.StoreApi;
import com.illegalaccess.delay.store.dto.*;
import com.illegalaccess.delay.store.entity.DelayMessageApp;
import com.illegalaccess.delay.store.entity.DelayMessageStat;
import com.illegalaccess.delay.store.entity.DelayMessageTopic;
import com.illegalaccess.delay.store.mysql.business.MySQLStoreBusiness;
import com.illegalaccess.delay.toolkit.dto.Pair;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * use mysql as persistence layer
 *
 * @author Jimmy Li
 * @date 2021-02-03 16:56
 */
@Slf4j
@Component
public class MySQLStoreApi implements StoreApi {

    @Autowired
    private MySQLStoreBusiness mySQLStoreBusiness;

    @Override
    public String persistDelayMessage(DelayMessageReq req, int slot) {
        return mySQLStoreBusiness.persistDelayMessage(req, slot);
    }

    @Override
    public List<DelayMessageDto> queryExpiringDelayMessage(Long lastId, LocalDateTime startTime, LocalDateTime endTime, List<Integer> slotList) {
        return mySQLStoreBusiness.queryExpiringDelayMessage(lastId, startTime, endTime, slotList);
    }

    @Override
    public List<DelayMessageDto> queryExpiringDelayMessage(LocalDateTime startTime, LocalDateTime endTime, List<Integer> slotList) {
        return mySQLStoreBusiness.queryExpiringDelayMessage(startTime, endTime, slotList);
    }

    @Override
    public boolean cancelDelayMessage(String messageId) {
        return mySQLStoreBusiness.cancelDelayMessage(messageId);
    }

    @Override
    public boolean completeDelayMessage(String messageId) {
        return mySQLStoreBusiness.completeDelayMessage(messageId);
    }

    @Override
    public boolean repeatDelayMessage(String messageId, Long execTime) {
        return mySQLStoreBusiness.repeatDelayMessage(messageId, execTime);
    }

    @Override
    public Long queryTotalMessageCount() {
        return mySQLStoreBusiness.queryTotalMessageCount();
    }

    @Override
    public Integer queryMessageSlot(String messageId, String appKey, String topic) {
        return mySQLStoreBusiness.queryMessageSlot(messageId, appKey, topic);
    }

    @Override
    public List<AppKeyMessageCntDto> queryMessageCount4AppKey(List<String> appKeyList) {
        return mySQLStoreBusiness.queryMessageCount4AppKey(appKeyList);
    }

    @Override
    public boolean archiveData(Date now) {
        return mySQLStoreBusiness.archiveData(now);
    }

    @Override
    public DelayMessageAppDto getAppMeta(String appKey) {
        return mySQLStoreBusiness.getAppMeta(appKey);
    }

    @Override
    public int saveDelayMessageSnapshot(List<Pair</*count*/Integer, /*snapshot time*/LocalDateTime>> list, String hostIp) {
        return mySQLStoreBusiness.saveDelayMessageSnapshot(list, hostIp);
    }

    /**
     * 获取所有appkey
     * @return
     */
    @Override
    public List<String> fetchAllAppKey() {
        return mySQLStoreBusiness.fetchAllAppKey();
    }

    /**
     * 存储统计信息
     * @param stats
     * @return
     */
    @Override
    public int saveDelayMessageStat(List<DelayMessageStat> stats) {
        return mySQLStoreBusiness.saveDelayMessageStat(stats);
    }

    @Override
    public boolean saveAppTopic(DelayMessageTopic delayMessageTopic) {
        return mySQLStoreBusiness.saveAppTopic(delayMessageTopic);
    }

    @Override
    public boolean updateAppTopic(Long id, Integer status) {
        return mySQLStoreBusiness.updateAppTopic(id, status);
    }

    @Override
    public List<DelayMessageTopicDto> queryTopics4App(String appkey) {
        return mySQLStoreBusiness.queryTopics4App(appkey);
    }

    @Override
    public boolean saveAppKeyInfo(DelayMessageApp delayMessageApp) {
        return mySQLStoreBusiness.saveAppKeyInfo(delayMessageApp);
    }

    @Override
    public List<String> queryAppKey4User(String creator) {
        return mySQLStoreBusiness.queryAppKey4User(creator);
    }

    @Override
    public List<DelayMessageAppDto> queryAppInfo4User(String creator) {
        return mySQLStoreBusiness.queryAppInfo4User(creator);
    }

    @Override
    public List<DelayMessageStat> queryAppTopicCntStat(QueryAppTopicStatStoreReq req) {
        return mySQLStoreBusiness.queryAppTopicCntStat(req);
    }
}
