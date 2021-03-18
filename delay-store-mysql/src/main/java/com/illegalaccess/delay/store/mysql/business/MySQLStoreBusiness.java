package com.illegalaccess.delay.store.mysql.business;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.illegalaccess.delay.cache.DelayCacheKey;
import com.illegalaccess.delay.cache.DelayCacheSupport;
import com.illegalaccess.delay.client.dto.DelayMessageReq;
import com.illegalaccess.delay.store.enums.DelayMessageStatusEnum;
import com.illegalaccess.delay.store.convert.StoreConverter;
import com.illegalaccess.delay.store.dto.*;
import com.illegalaccess.delay.store.entity.*;
import com.illegalaccess.delay.store.mysql.service.*;
import com.illegalaccess.delay.toolkit.IdGenerator;
import com.illegalaccess.delay.toolkit.TimeUtils;
import com.illegalaccess.delay.toolkit.dto.Pair;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Jimmy Li
 * @date 2021-03-04 10:29
 */
@Slf4j
@Component
public class MySQLStoreBusiness {

    @Autowired
    private IDelayMessageService delayMessageService;
    @Autowired
    private IDelayMessageStatService delayMessageStatService;
    @Autowired
    private IDelayMessageAppService delayMessageAppService;
    @Autowired
    private IDelayMessageSnapshotService delayMessageSnapshotService;
    @Autowired
    private IDelayMessageTopicService delayMessageTopicService;
    @Autowired
    private StoreConverter storeConverter;
    @Autowired
    private DelayCacheSupport delayCacheSupport;
    @Autowired
    private DelayCacheKey delayCacheKey;

    private final int batch = 200;

    public String persistDelayMessage(DelayMessageReq req, int slot) {
        DelayMessage delayMessage = storeConverter.toMessageEntity(req);
//        delayMessage.setExecTime(req.getExecTime());
        delayMessage.setSlot(slot);
        delayMessage.setMsgId(IdGenerator.generate());
        delayMessage.setModifyTime(LocalDateTime.now());

        delayMessageService.save(delayMessage);
        log.info("a delay message is saved");
        return delayMessage.getMsgId();
    }


    public List<DelayMessageDto> queryExpiringDelayMessage(Long lastId, LocalDateTime startTime, LocalDateTime endTime, List<Integer> slotList) {
        log.info("query expiring message for lastId:{}, slotList:{}", lastId, slotList);
        List<DelayMessage> delayMessages = delayMessageService.lambdaQuery()
                .select(DelayMessage::getMsgId, DelayMessage::getAppKey, DelayMessage::getExecTime, DelayMessage::getTopic, DelayMessage::getMsgContent)
                .gt(DelayMessage::getId, lastId)
                .in(DelayMessage::getSlot, slotList)
                .le(DelayMessage::getExecTime, TimeUtils.toTimeStamp(endTime))
                .ge(DelayMessage::getExecTime, TimeUtils.toTimeStamp(startTime))
                .eq(DelayMessage::getStatus, 1)
                .list();
        log.info("get expiring msg cnt:{}", delayMessages.size());
        return storeConverter.toDelayMessageDtoList(delayMessages);
    }


    public List<DelayMessageDto> queryExpiringDelayMessage(LocalDateTime startTime, LocalDateTime endTime, List<Integer> slotList) {
        log.info("query expiring message between {} and {}, slotList:{}", startTime, endTime, slotList);
        List<DelayMessage> delayMessages = delayMessageService.lambdaQuery()
                .select(DelayMessage::getMsgId, DelayMessage::getAppKey, DelayMessage::getExecTime, DelayMessage::getTopic, DelayMessage::getMsgContent, DelayMessage::getRepeatInterval)
                .in(DelayMessage::getSlot, slotList)
                .le(DelayMessage::getExecTime, TimeUtils.toTimeStamp(endTime))
                .ge(DelayMessage::getExecTime, TimeUtils.toTimeStamp(startTime))
                .eq(DelayMessage::getStatus, 1)
                .orderByAsc(DelayMessage::getExecTime)
                .list();
        log.info("get expiring msg cnt:{}", delayMessages.size());
        return storeConverter.toDelayMessageDtoList(delayMessages);
    }

    public boolean cancelDelayMessage(String messageId) {
        Wrapper where = new QueryWrapper<>().eq("msg_id", messageId);
        boolean removed = delayMessageService.remove(where);
        log.info("remove msg:{} res:{}", messageId, removed);
        return removed;
    }


    public boolean completeDelayMessage(String messageId) {
        boolean complete = delayMessageService.updateDelayMessage(messageId, DelayMessageStatusEnum.Sent);
        log.info("delay message:{} is sent", messageId);
        return complete;
    }

    public boolean repeatDelayMessage(String messageId, Long execTime) {
        UpdateWrapper<DelayMessage> uw = new UpdateWrapper<>();
        uw.set("exec_time", execTime).eq("msg_id", messageId);
        log.info("msg:{} set exec time to:{}", messageId, execTime);
        return delayMessageService.update(uw);
    }


    public Long queryTotalMessageCount() {
        return null;
    }


    public List<AppKeyMessageCntDto> queryMessageCount4AppKey(List<String> appKeyList) {
        return null;
    }


    public boolean archiveData(Date now) {
        log.info("will archive completed and canclled message before:{}", now);
        // todo 需要传递slot数组？？？？
        delayMessageService.archiveDelayMessage(now);
        return true;
    }


    public DelayMessageAppDto getAppMeta(String appKey) {
        String cacheKey = delayCacheKey.createCacheKey4AppKey(appKey);
        DelayMessageAppDto data = delayCacheSupport.getValueFromCache(cacheKey, DelayMessageAppDto.class, 1000L, TimeUnit.DAYS, (t) -> {
            DelayMessageApp delayMessageApp = delayMessageAppService.getOne(Wrappers.<DelayMessageApp>lambdaQuery().eq(DelayMessageApp::getAppKey, appKey));
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

        boolean saved = delayMessageSnapshotService.saveBatch(data, batch);
        return saved ? list.size() : 0;
    }

    /**
     * 获取所有appkey
     * @return
     */
    public List<String> fetchAllAppKey() {
        LambdaQueryWrapper<DelayMessageApp> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(DelayMessageApp::getAppKey).eq(DelayMessageApp::getStatus, 1);

        return delayMessageAppService.listObjs(queryWrapper, o -> o.toString());
    }

    /**
     * 存储统计信息
     * @param stats
     * @return
     */
    public int saveDelayMessageStat(List<DelayMessageStat> stats) {
        boolean saved = delayMessageStatService.saveBatch(stats, batch);
        return saved ? stats.size() : 0;
    }

    public boolean saveAppTopic(DelayMessageTopic delayMessageTopic) {
        return delayMessageTopicService.save(delayMessageTopic);
    }

    public boolean updateAppTopic(Long id, Integer status) {
        DelayMessageTopic topic = new DelayMessageTopic();
        topic.setStatus(status);
        Wrapper where = new QueryWrapper<>().eq("id", id);
        boolean update = delayMessageTopicService.update(topic, where);
        log.info("update topic:{} status:{} result:{}", id, status, update);
        return update;
    }

    public List<DelayMessageTopicDto> queryTopics4App(String appkey) {
        LambdaQueryWrapper<DelayMessageTopic> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DelayMessageTopic::getAppKey, appkey);
        List<DelayMessageTopic> topics = delayMessageTopicService.list(queryWrapper);
        return storeConverter.toDelayMessageTopicDtoList(topics);
    }

    public boolean saveAppKeyInfo(DelayMessageApp delayMessageApp) {
        return delayMessageAppService.save(delayMessageApp);
    }

    public List<String> queryAppKey4User(String creator) {
        LambdaQueryWrapper<DelayMessageApp> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .select(DelayMessageApp::getAppKey)
                .eq(DelayMessageApp::getCreator, creator)
                .eq(DelayMessageApp::getStatus, 1);

        return delayMessageAppService.listObjs(queryWrapper, o -> o.toString());
    }

    public List<DelayMessageAppDto> queryAppInfo4User(String creator) {
        LambdaQueryWrapper<DelayMessageApp> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(DelayMessageApp::getCreator, creator)
                .eq(DelayMessageApp::getStatus, 1);

        List<DelayMessageApp> app = delayMessageAppService.list(queryWrapper);
        return storeConverter.toDelayMessageAppDtoList(app);
    }

    public List<DelayMessageStat> queryAppTopicCntStat(QueryAppTopicStatStoreReq req) {

        return delayMessageStatService.lambdaQuery().eq(DelayMessageStat::getAppKey, req.getAppKey())
                .in(!CollectionUtils.isEmpty(req.getTopics()), DelayMessageStat::getTopic, req.getTopics())
                .ge(DelayMessageStat::getMessageReceivedTime, req.getStart())
                .le(DelayMessageStat::getMessageReceivedTime, req.getEnd())
                .list();
    }
}
