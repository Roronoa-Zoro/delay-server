package com.illegalaccess.delay.core.business;

import com.illegalaccess.delay.cache.DelayCacheKey;
import com.illegalaccess.delay.cache.DelayCacheSupport;
import com.illegalaccess.delay.core.transport.DelayCoreConverter;
import com.illegalaccess.delay.store.StoreApi;
import com.illegalaccess.delay.store.dto.DelayMessageAppDto;
import com.illegalaccess.delay.store.dto.DelayMessageTopicDto;
import com.illegalaccess.delay.store.dto.QueryAppTopicStatStoreReq;
import com.illegalaccess.delay.store.entity.DelayMessageApp;
import com.illegalaccess.delay.store.entity.DelayMessageStat;
import com.illegalaccess.delay.store.entity.DelayMessageTopic;
import com.illegalaccess.delay.toolkit.IdGenerator;
import com.illegalaccess.delay.toolkit.TimeUtils;
import com.illegalaccess.delay.toolkit.dto.BaseResponse;
import com.illegalaccess.delay.toolkit.dto.PageRequest;
import com.illegalaccess.delay.toolkit.dto.Pair;
import com.illegalaccess.delay.ui.client.dto.TrendInfo;
import com.illegalaccess.delay.ui.client.dto.app.*;
import com.illegalaccess.delay.ui.client.dto.system.QueryServerLoadReq;
import com.illegalaccess.delay.ui.client.dto.system.QueryServerLoadResp;
import com.illegalaccess.delay.ui.client.dto.topic.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.*;

/**
 * 延时管理后台对应的业务操作
 * @author Jimmy Li
 */
@Component
@Slf4j
public class DelayUIBusiness {

    @Autowired
    private DelayCacheSupport delayCacheSupport;
    @Autowired
    private DelayCacheKey delayCacheKey;
    @Autowired
    private StoreApi storeApi;
    @Autowired
    private DelayCoreConverter delayCoreConverter;

    public BaseResponse<Long> queryTotalMessage() {
        String key = delayCacheKey.createTotalMsgCntKey();
        Long cnt = delayCacheSupport.getValueFromCache(key, Long.class);
        return BaseResponse.success(cnt);
    }

    public BaseResponse<Boolean> queryTopicForApp(QueryApp4TopicReq req) {
        List<DelayMessageTopicDto> resp = storeApi.queryTopics4App(req.getAppKey());
        if (CollectionUtils.isEmpty(resp)) {
            log.info("app:{} and topic:{} not exist", req.getAppKey(), req.getTopic());
            return BaseResponse.success(false);
        }

        for (DelayMessageTopicDto delayMessageTopicDto : resp) {
            if (delayMessageTopicDto.getTopic().equals(req.getTopic())) {
                log.info("app:{} and topic:{} exist", req.getAppKey(), req.getTopic());
                return BaseResponse.success(true);
            }
        }

        log.info("app:{} and topic:{} not exist", req.getAppKey(), req.getTopic());
        return BaseResponse.success(false);
    }
    
    
    public BaseResponse<SaveTopicResp> saveTopic(SaveTopicReq req) {
        LocalDateTime now = LocalDateTime.now();
        DelayMessageTopic topic = delayCoreConverter.toDelayMessageTopic(req);
        topic.setCreateTime(now);
        topic.setUpdateTime(now);
        topic.setId(System.currentTimeMillis());
        boolean saved = storeApi.saveAppTopic(topic);
        return BaseResponse.success(SaveTopicResp.builder().id(topic.getId()).build());
    }
    
    
    public BaseResponse<UpdateTopicResp> updateTopic(UpdateTopicReq req) {
        return null;
    }
    
    
    public BaseResponse<QueryTopicResp> queryTopics(QueryTopicReq req) {
        List<DelayMessageTopicDto> resp = storeApi.queryTopics4App(req.getAppKey());
        List<QueryTopicInfo> data = delayCoreConverter.toQueryTopicInfoList(resp);
        return BaseResponse.success(QueryTopicResp.builder().data(data).build());
    }
    
    
    public BaseResponse<Integer> queryTotalAppKey() {
        List<String> keys = storeApi.fetchAllAppKey();
        return BaseResponse.success(keys.size());
    }

    public BaseResponse<SaveAppKeyResp> saveAppKey(SaveAppKeyReq req) {
        DelayMessageApp delayMessageApp = delayCoreConverter.toDelayMessageApp(req);
        delayMessageApp.setAppId(IdGenerator.uuid());
        delayMessageApp.setAppKey(IdGenerator.uuid());
        delayMessageApp.setAppSecret(IdGenerator.uuid());
        delayMessageApp.setCreateTime(LocalDateTime.now());
        delayMessageApp.setUpdateTime(LocalDateTime.now());
        boolean saved = storeApi.saveAppKeyInfo(delayMessageApp);
        return BaseResponse.success(delayCoreConverter.toSaveAppKeyResp(delayMessageApp));
    }

    
    public BaseResponse<UpdateAppKeyResp> updateAppKey(UpdateAppKeyReq req) {

        return null;
    }

    
    public BaseResponse<QueryAppKeyResp> queryAppKey(QueryAppKeyReq req) {
        List<DelayMessageAppDto> dto = storeApi.queryAppInfo4User(req.getCreator());
        return BaseResponse.success(QueryAppKeyResp.builder().data(delayCoreConverter.toQueryAppKeyRespList(dto)).build());
    }
    
    
    public BaseResponse<QueryAppKeyResp> queryAllAppKey(PageRequest req) {
        List<String> allApp = storeApi.fetchAllAppKey();
        if (CollectionUtils.isEmpty(allApp)) {
            log.info("does not get all app key");
            return BaseResponse.success(QueryAppKeyResp.builder().build());
        }

        List<QueryAppKeyInfo> data = new ArrayList<>(allApp.size());
        allApp.forEach(app -> data.add(QueryAppKeyInfo.builder().appKey(app).build()));
        return BaseResponse.success(QueryAppKeyResp.builder().data(data).build());
    }

    /**
     * 查询App下topic的消息数量趋势
     * @param req
     * @return
     */
    public BaseResponse<QueryAppTopicStatResp> queryAppTopicStat(QueryAppTopicStatReq req) {
        QueryAppTopicStatStoreReq storeReq = delayCoreConverter.toQueryAppTopicStatStoreReq(req);
        if (storeReq.getStart() == null) {
            storeReq.setStart(LocalDateTime.now().minusHours(1L));
        }

        if (storeReq.getEnd() == null) {
            storeReq.setEnd(LocalDateTime.now().plusHours(1L));
        }
        List<DelayMessageStat> list = storeApi.queryAppTopicCntStat(storeReq);
        log.info("does not get data");
        if (CollectionUtils.isEmpty(list)) {
            return BaseResponse.success(QueryAppTopicStatResp.builder().appKey(req.getAppKey()).build());
        }

        Set<String> timeSet = new LinkedHashSet<>();
        List<Pair</*topic*/String, /*数据值*/List<Integer>>> data = new ArrayList<>();
        Map</*topic*/String, List<Integer>> topicMap = new HashMap<>();

        list.forEach(stat -> {
            timeSet.add(TimeUtils.formatLocalDateTime(stat.getMessageReceivedTime()));
            List<Integer> cntList = topicMap.get(stat.getTopic());
            if (cntList == null) {
                cntList = new ArrayList<>();
                topicMap.put(stat.getTopic(), cntList);
            }

            cntList.add(stat.getMessageReceivedCnt());
        });

        topicMap.forEach((k, v) -> {
            Pair<String, List<Integer>> pair = new Pair<>(k,v);
            data.add(pair);
        });

        TrendInfo ti = TrendInfo.builder().xContent(new ArrayList<>(timeSet)).data(data).build();
        return BaseResponse.success(QueryAppTopicStatResp.builder().appKey(req.getAppKey()).trendInfo(ti).build());
    }
    
    
    public BaseResponse<QueryServerLoadResp> queryServerLoad(QueryServerLoadReq req) {
        return null;
    }
}
