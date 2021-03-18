package com.illegalaccess.delay.exporter.rpc;


import com.illegalaccess.delay.core.business.DelayUIBusiness;
import com.illegalaccess.delay.toolkit.annotation.API;
import com.illegalaccess.delay.toolkit.dto.BaseResponse;
import com.illegalaccess.delay.toolkit.dto.PageRequest;
import com.illegalaccess.delay.ui.client.DelayUIClient;
import com.illegalaccess.delay.ui.client.dto.app.*;
import com.illegalaccess.delay.ui.client.dto.system.QueryServerLoadReq;
import com.illegalaccess.delay.ui.client.dto.system.QueryServerLoadResp;
import com.illegalaccess.delay.ui.client.dto.topic.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *  管理后台接口实现, 目前支持dubbo 和 http 协议
 * @author Jimmy Li
 * @date 2021-03-04 10:20
 */
@Path("/delay/ui")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
@Component
@DubboService(interfaceClass = DelayUIClient.class)
public class DelayUIClientImpl implements DelayUIClient {

    @Autowired
    private DelayUIBusiness delayUIBusiness;


    @API
    @POST
    @Path("/queryTotalMessage")
    @Override
    public BaseResponse<Long> queryTotalMessage() {
        return delayUIBusiness.queryTotalMessage();
    }

    @API
    @POST
    @Path("/queryTopicForApp")
    @Override
    public BaseResponse<Boolean> queryTopicForApp(QueryApp4TopicReq req) {
        return delayUIBusiness.queryTopicForApp(req);
    }

    @API
    @POST
    @Path("/saveTopic")
    @Override
    public BaseResponse<SaveTopicResp> saveTopic(SaveTopicReq req) {
        return delayUIBusiness.saveTopic(req);
    }

    @API
    @POST
    @Path("/updateTopic")
    @Override
    public BaseResponse<UpdateTopicResp> updateTopic(UpdateTopicReq req) {
        return delayUIBusiness.updateTopic(req);
    }

    @API
    @POST
    @Path("/queryTopics")
    @Override
    public BaseResponse<QueryTopicResp> queryTopics(QueryTopicReq req) {
        return delayUIBusiness.queryTopics(req);
    }

    @API
    @POST
    @Path("/queryTotalAppKey")
    @Override
    public BaseResponse<Integer> queryTotalAppKey() {
        return delayUIBusiness.queryTotalAppKey();
    }

    @API
    @POST
    @Path("/saveAppKey")
    @Override
    public BaseResponse<SaveAppKeyResp> saveAppKey(SaveAppKeyReq req) {
        return delayUIBusiness.saveAppKey(req);
    }

    @API
    @POST
    @Path("/updateAppKey")
    @Override
    public BaseResponse<UpdateAppKeyResp> updateAppKey(UpdateAppKeyReq req) {
        return delayUIBusiness.updateAppKey(req);
    }

    @API
    @POST
    @Path("/queryAppKey")
    @Override
    public BaseResponse<QueryAppKeyResp> queryAppKey(QueryAppKeyReq req) {
        return delayUIBusiness.queryAppKey(req);
    }

    @API
    @POST
    @Path("/queryAllAppKey")
    @Override
    public BaseResponse<QueryAppKeyResp> queryAllAppKey(PageRequest req) {
        return delayUIBusiness.queryAllAppKey(req);
    }

    @API(logResp = false)
    @POST
    @Path("/queryAppTopicStat")
    @Override
    public BaseResponse<QueryAppTopicStatResp> queryAppTopicStat(QueryAppTopicStatReq req) {
        BaseResponse<QueryAppTopicStatResp> resp = delayUIBusiness.queryAppTopicStat(req);
        return resp;
    }

    @API(logResp = false)
    @POST
    @Path("/queryServerLoad")
    @Override
    public BaseResponse<QueryServerLoadResp> queryServerLoad(QueryServerLoadReq req) {
        return delayUIBusiness.queryServerLoad(req);
    }
}
