package com.illegalaccess.delay.client;

import com.illegalaccess.delay.client.dto.CancelMessageReq;
import com.illegalaccess.delay.client.dto.CancelMessageResp;
import com.illegalaccess.delay.client.dto.DelayMessageReq;
import com.illegalaccess.delay.client.dto.DelayMessageResp;
import com.illegalaccess.delay.toolkit.dto.*;
/**
 * delay message api
 * @date 2021-03-04 10:16
 * @author Jimmy Li
 */
public interface DelayMessageApi {

    /**
     * send delay message
     *
     * @param req
     * @return
     */
    BaseResponse<DelayMessageResp> sendDelayMsg(DelayMessageReq req);

    /**
     * cancel sent delay message
     *
     * @param req
     * @return
     */
    BaseResponse<CancelMessageResp> cancelDelayMsg(CancelMessageReq req);
}
