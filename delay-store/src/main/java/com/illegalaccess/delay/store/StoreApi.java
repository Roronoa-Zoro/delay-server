package com.illegalaccess.delay.store;

import com.illegalaccess.delay.client.dto.DelayMessageReq;
import com.illegalaccess.delay.store.dto.*;
import com.illegalaccess.delay.store.entity.DelayMessageApp;
import com.illegalaccess.delay.store.entity.DelayMessageStat;
import com.illegalaccess.delay.store.entity.DelayMessageTopic;
import com.illegalaccess.delay.toolkit.dto.Pair;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Persistence layer api
 *
 * @author Jimmy Li
 * @date 2021-02-02 20:14
 */
public interface StoreApi {

    /**
     * save delay message
     *
     * @param req
     * @return
     */
    String persistDelayMessage(DelayMessageReq req, int slot);

    /**
     * query expiring message within withinTime
     *
     * @param lastId
     * @param startTime
     * @param endTime
     * @return
     */
    List<DelayMessageDto> queryExpiringDelayMessage(Long lastId, LocalDateTime startTime, LocalDateTime endTime, List<Integer> slotList);

    /**
     *
     * @param startTime
     * @param endTime
     * @param slotList
     * @return
     */
    List<DelayMessageDto> queryExpiringDelayMessage(LocalDateTime startTime, LocalDateTime endTime, List<Integer> slotList);

    /**
     * cancel a received delay message
     *
     * @param messageId
     * @return
     */
    boolean cancelDelayMessage(String messageId);

    /**
     * update delay message to sent status
     * @param messageId
     * @return
     */
    boolean completeDelayMessage(String messageId);

    /**
     * 重复延时消息
     * @param messageId
     * @return
     */
    boolean repeatDelayMessage(String messageId, Long execTime);

    /**
     * query all received message count
     *
     * @return
     */
    Long queryTotalMessageCount();

    /**
     * query message count for given app key list
     *
     * @param appKeyList
     * @return
     */
    List<AppKeyMessageCntDto> queryMessageCount4AppKey(List<String> appKeyList);

    /**
     * 归档已处理的数据
     * @param now 执行归档操作时的时间
     * @return
     */
    boolean archiveData(Date now);

    /**
     * get meta data for appKey
     * @param appKey
     * @return
     */
    DelayMessageAppDto getAppMeta(String appKey);



    /**======================================== api for admin ========================================**/
    /**
     1. apply appkey
     2. grant appkey to others, make it in admin
     3. query all appkey
     4. query stat info for appkey
     5. query user appkey and topic
     6. query all message cnt
     */

    default boolean saveAppTopic(DelayMessageTopic delayMessageTopic) {
        return true;
    }

    default boolean updateAppTopic(Long id, Integer status) {
        return true;
    }

    default List<DelayMessageTopicDto> queryTopics4App(String appkey) {
        return null;
    }

    default boolean saveAppKeyInfo(DelayMessageApp delayMessageApp) {
        return true;
    }

    /**
     * 获取所有appkey
     * @return
     */
    default List<String> fetchAllAppKey() {
        return new ArrayList<>(0);
    }

    /**
     * 查询指定用户创建的appkey
     * @param creator
     * @return
     */
    default List<String> queryAppKey4User(String creator) {
        return new ArrayList<>(0);
    }

    /**
     * 查询指定用户创建的appkey
     * @param creator
     * @return
     */
    default List<DelayMessageAppDto> queryAppInfo4User(String creator) {
        return new ArrayList<>(0);
    }

    /**
     * 批量保存内存里面消息数量的快照
     * @return
     */
    default int saveDelayMessageSnapshot(List<Pair</*count*/Integer, /*snapshot time*/LocalDateTime>> list, String hostIp) {
        return 0;
    }



    /**
     * 存储统计信息
     * @param stats
     * @return
     */
    default int saveDelayMessageStat(List<DelayMessageStat> stats) {
        return 0;
    }

    /**
     * 按App+topic + 时间区间 查询消息的数量
     * @param req
     * @return
     */
    default List<DelayMessageStat> queryAppTopicCntStat(QueryAppTopicStatStoreReq req) {
        return null;
    }
}
