package com.illegalaccess.delay.exporter.rpc;

import com.illegalaccess.delay.client.DelayMessageApi;
import com.illegalaccess.delay.client.dto.CancelMessageReq;
import com.illegalaccess.delay.client.dto.CancelMessageResp;
import com.illegalaccess.delay.client.dto.DelayMessageReq;
import com.illegalaccess.delay.client.dto.DelayMessageResp;
import com.illegalaccess.delay.core.state.DelayServerContext;
import com.illegalaccess.delay.toolkit.dto.BaseResponse;
import com.illegalaccess.delay.toolkit.enums.DelayBusinessEnum;
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
 * 接口实现, 目前支持dubbo 和 http 协议
 * @author Jimmy Li
 * @date 2021-03-04 10:20
 */
@Path("/delay")
@Slf4j
@Component
@DubboService(interfaceClass = DelayMessageApi.class)
public class DelayMessageServerApi implements DelayMessageApi {

    @Autowired
    private DelayServerContext delayServerContext;

    @POST
    @Path("/sendDelayMsg")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public BaseResponse<DelayMessageResp> sendDelayMsg(DelayMessageReq req) {
        String messageId = delayServerContext.acceptMessage(req);
        log.info("receive a delay message and associated a messageId:{}", messageId);
        DelayMessageResp resp = new DelayMessageResp(messageId);
        return BaseResponse.success(resp);
    }

    @POST
    @Path("/cancelDelayMsg")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public BaseResponse<CancelMessageResp> cancelDelayMsg(CancelMessageReq req) {
        // 取消已发送的延时消息
        String msgId = delayServerContext.cancelMessage(req);
        if (req.getMessageId().equals(msgId)) {
            return BaseResponse.success(CancelMessageResp.builder().messageId(msgId).build());
        }
        return BaseResponse.fail(DelayBusinessEnum.Cancel_Msg_Fail);
    }
}
