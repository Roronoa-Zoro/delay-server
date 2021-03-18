package com.illegalaccess.delay.store.cassandra.service;

import com.illegalaccess.delay.cache.DelayCacheKey;
import com.illegalaccess.delay.cache.DelayCacheSupport;
import com.illegalaccess.delay.client.dto.DelayMessageReq;
import com.illegalaccess.delay.store.enums.DelayMessageStatusEnum;
import com.illegalaccess.delay.store.cassandra.CassandraStoreConverter;
import com.illegalaccess.delay.store.cassandra.entity.DelayMessage;
import com.illegalaccess.delay.store.cassandra.entity.DelayMessageApp;
import com.illegalaccess.delay.store.cassandra.repository.DelayMessageAppRepository;
import com.illegalaccess.delay.store.cassandra.repository.DelayMessageStatRepository;
import com.illegalaccess.delay.store.dto.AppKeyMessageCntDto;
import com.illegalaccess.delay.store.dto.DelayMessageAppDto;
import com.illegalaccess.delay.store.dto.DelayMessageDto;
import com.illegalaccess.delay.store.entity.DelayMessageSnapshot;
import com.illegalaccess.delay.store.entity.DelayMessageStat;
import com.illegalaccess.delay.toolkit.IdGenerator;
import com.illegalaccess.delay.toolkit.TimeUtils;
import com.illegalaccess.delay.toolkit.dto.Pair;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraBatchOperations;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.query.Criteria;
import org.springframework.data.cassandra.core.query.Query;
import org.springframework.data.cassandra.core.query.Update;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


/**
 *
 * @author Jimmy Li
 * @date 2021-03-01 16:47
 */
@Slf4j
@Component
public class CassandraStoreService {

    @Autowired
    private CassandraOperations cassandraOperations;
    @Autowired
    private CassandraStoreConverter storeConverter;
    @Autowired
    private DelayCacheSupport delayCacheSupport;
    @Autowired
    private DelayCacheKey delayCacheKey;
    @Autowired
    private DelayMessageAppRepository delayMessageAppRepository;
    @Autowired
    private DelayMessageStatRepository delayMessageStatRepository;

    /**
     * save delay message to cassandra
     * @param req
     * @param slot
     * @return
     */
    public String persistDelayMessage(DelayMessageReq req, int slot) {
        DelayMessage delayMessage = storeConverter.toMessageEntity(req);
//        delayMessage.setId(IdGenerator.longId());
        delayMessage.setSlot(slot);
        delayMessage.setMsgId(IdGenerator.generate());
        delayMessage.setModifyTime(LocalDateTime.now());
        delayMessage.setExecTime(req.getExecTime());
        cassandraOperations.insert(delayMessage);
        log.info("a delay message is saved");
        return delayMessage.getMsgId();
    }

    public List<DelayMessageDto> queryExpiringDelayMessage(Long lastId, LocalDateTime startTime, LocalDateTime endTime, List<Integer> slotList) {
        return null;
    }

    /**
     * query expiring data
     * @param startTime
     * @param endTime
     * @param slotList
     * @return
     */
    public List<DelayMessageDto> queryExpiringDelayMessage(LocalDateTime startTime, LocalDateTime endTime, List<Integer> slotList) {
        Query query = Query.query(Criteria.where("exec_time").gte(TimeUtils.toTimeStamp(startTime)))
                .and(Criteria.where("exec_time").lte(TimeUtils.toTimeStamp(endTime)))
                .and(Criteria.where("slot").in(slotList))
                .and(Criteria.where("status").is(DelayMessageStatusEnum.Valid.getStatus()))
                .sort(Sort.by(Sort.Order.asc("exec_time")))
                .withAllowFiltering()
        ;
        List<DelayMessage> delayMessages = cassandraOperations.select(query, DelayMessage.class);
        return storeConverter.toDelayMessageDtoList(delayMessages);
    }

    /**
     * mark message canclled
     * @param messageId
     * @return
     */
    public boolean cancelDelayMessage(String messageId) {
        Query query = Query.query(Criteria.where("msg_id").is(messageId));
        boolean update = cassandraOperations.update(query, Update.update("status", DelayMessageStatusEnum.InValid.getStatus()), DelayMessage.class);

        return update;
    }

    /**
     * mark message completed
     * @param messageId
     * @return
     */
    public boolean completeDelayMessage(String messageId) {
        Query query = Query.query(Criteria.where("msg_id").is(messageId));
        boolean update = cassandraOperations.update(query, Update.update("status", DelayMessageStatusEnum.Sent.getStatus()), DelayMessage.class);

        return update;
    }

    public boolean repeatDelayMessage(String messageId, Long execTime) {
        Query query = Query.query(Criteria.where("msg_id").is(messageId));
        boolean update = cassandraOperations.update(query, Update.update("exec_time", execTime), DelayMessage.class);
        return update;
    }

    public Long queryTotalMessageCount() {
        return null;
    }

    public List<AppKeyMessageCntDto> queryMessageCount4AppKey(List<String> appKeyList) {
        return null;
    }

    public boolean archiveData(Date now) {
        return false;
    }

    public DelayMessageAppDto getAppMeta(String appKey) {
        String cacheKey = delayCacheKey.createCacheKey4AppKey(appKey);
        DelayMessageAppDto data = delayCacheSupport.getValueFromCache(cacheKey, DelayMessageAppDto.class, 1000L, TimeUnit.DAYS, (t) -> {
            Query query = Query.query(Criteria.where("app_key").is(appKey))
                    .and(Criteria.where("status").is(DelayMessageStatusEnum.Valid.getStatus()))
                    .withAllowFiltering();
            DelayMessageApp delayMessageApp = cassandraOperations.selectOne(query, DelayMessageApp.class);

            DelayMessageAppDto delayMessageAppDto = storeConverter.toDelayMessageAppDto(delayMessageApp);
            return delayMessageAppDto;
        }, appKey);

        return data;
    }

    public int saveDelayMessageSnapshot(List<Pair</*count*/Integer, /*snapshot time*/LocalDateTime>> list, String hostIp) {
        List<DelayMessageSnapshot> data = new ArrayList<>(list.size());
        list.forEach(pair -> {
            DelayMessageSnapshot snapshot = new DelayMessageSnapshot();
            snapshot.setHostIp(hostIp);
            snapshot.setMessageCnt(pair.getFirst());
            snapshot.setSnapshotTime(pair.getSecond());
            data.add(snapshot);
        });
        CassandraBatchOperations batchOps = cassandraOperations.batchOps().insert(data);
        batchOps.execute();
        return list.size();
    }

    /**
     * 获取所有appkey
     * @return
     */
    public List<String> fetchAllAppKey() {
        List<DelayMessageApp> list = delayMessageAppRepository.findAll();
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>(0);
        }
        return list.stream().map(DelayMessageApp::getAppKey).collect(Collectors.toList());
    }

    /**
     * 存储统计信息
     * @param stats
     * @return
     */
    public int saveDelayMessageStat(List<DelayMessageStat> stats) {
        CassandraBatchOperations batchOps = cassandraOperations.batchOps().insert(stats);
        batchOps.execute();
        return stats.size();
    }
}
