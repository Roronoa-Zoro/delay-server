package com.illegalaccess.delay.ui.client;

import com.illegalaccess.delay.toolkit.dto.BaseResponse;
import com.illegalaccess.delay.toolkit.dto.PageRequest;
import com.illegalaccess.delay.ui.client.dto.app.*;
import com.illegalaccess.delay.ui.client.dto.system.QueryServerLoadReq;
import com.illegalaccess.delay.ui.client.dto.system.QueryServerLoadResp;
import com.illegalaccess.delay.ui.client.dto.topic.*;


/**
 * 给UI使用的客户端
 * @author Jimmy Li
 * @date 2021-03-10 21:14
 */
public interface DelayUIClient {

    /**
     * 查询接收到的消息总量
     * @return
     */
    BaseResponse<Long> queryTotalMessage();

    /* api for topic begin */

    /**
     * 根据appkey + topic查询是否存在数据
     * @param req
     * @return
     */
    BaseResponse<Boolean> queryTopicForApp(QueryApp4TopicReq req);

    /**
     * 保存topic到App下
     * @param req
     * @return
     */
    BaseResponse<SaveTopicResp> saveTopic(SaveTopicReq req);

    /**
     * 更新topic状态
     * @param req
     * @return
     */
    BaseResponse<UpdateTopicResp> updateTopic(UpdateTopicReq req);

    /**
     * 查询App下的topics
     * @param req
     * @return
     */
    BaseResponse<QueryTopicResp> queryTopics(QueryTopicReq req);

    /* api for topic  end **/


    /* api for app begin **/

    // 1.appkey的管理接口 2.appkey相关的topic对应的消息数量的接口

    /**
     * 查询接入的appkey总量
     * @return
     */
    BaseResponse<Integer> queryTotalAppKey();

    /**
     * 申请/保存 appkey
     * @param req
     * @return
     */
    BaseResponse<SaveAppKeyResp> saveAppKey(SaveAppKeyReq req);

    /**
     * 更新appkey 信息
     * @param req
     * @return
     */
    BaseResponse<UpdateAppKeyResp> updateAppKey(UpdateAppKeyReq req);

    /**
     * 按用户查appkey
     * @param req
     * @return
     */
    BaseResponse<QueryAppKeyResp> queryAppKey(QueryAppKeyReq req);

    /**
     * 查询所有appkey 信息，超级管理员接口
     * @param req
     * @return
     */
    BaseResponse<QueryAppKeyResp> queryAllAppKey(PageRequest req);


    BaseResponse<QueryAppTopicStatResp> queryAppTopicStat(QueryAppTopicStatReq req);

    /* api for app end **/

    /* api for delay server load  begin */

    /**
     * 查询delay server 系统负载快照
     * @param req
     * @return
     */
    BaseResponse<QueryServerLoadResp> queryServerLoad(QueryServerLoadReq req);

    /* api for delay server load end **/
}
