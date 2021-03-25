package com.illegalaccess.delay.core.transport;

import com.illegalaccess.delay.client.dto.DelayMessageReq;
import com.illegalaccess.delay.core.delay.DelayMessageObj;
import com.illegalaccess.delay.store.dto.*;
import com.illegalaccess.delay.store.entity.DelayMessageApp;
import com.illegalaccess.delay.store.entity.DelayMessageTopic;
import com.illegalaccess.delay.ui.client.dto.app.QueryAppKeyInfo;
import com.illegalaccess.delay.ui.client.dto.app.QueryAppTopicStatReq;
import com.illegalaccess.delay.ui.client.dto.app.SaveAppKeyReq;
import com.illegalaccess.delay.ui.client.dto.app.SaveAppKeyResp;
import com.illegalaccess.delay.ui.client.dto.system.QueryServerLoadReq;
import com.illegalaccess.delay.ui.client.dto.topic.QueryTopicInfo;
import com.illegalaccess.delay.ui.client.dto.topic.SaveTopicReq;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

/**
 * 对象转换
 */
@Mapper(componentModel = "spring")
public interface DelayCoreConverter {

    @Mappings(value = {
            @Mapping(target = "message", source = "msgContent"),
            @Mapping(target = "messageId", source = "msgId"),
            @Mapping(target = "ttl", source = "execTime")
    })
    DelayMessageObj toDelayMessageObj(DelayMessageDto delayMessageDto);

    @Mappings(value = {
            @Mapping(target = "messageId", source = "messageId"),
            @Mapping(target = "repeatInterval", constant = "0L"),
            @Mapping(target = "ttl", source = "delayMessageReq.execTime")
    })
    DelayMessageObj toDelayMessageObj(DelayMessageReq delayMessageReq, String messageId);

    List<DelayMessageObj> toDelayMessageObjList(List<DelayMessageDto> delayMessageDtoList);

    @Mappings(value = {
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "mqType", source = "mqType.type"),
            @Mapping(target = "status", constant = "1")
    })
    DelayMessageTopic toDelayMessageTopic(SaveTopicReq req);

    QueryTopicInfo toQueryTopicInfo(DelayMessageTopicDto req);

    List<QueryTopicInfo> toQueryTopicInfoList(List<DelayMessageTopicDto> req);

    @Mappings(value = {
            @Mapping(target = "appId", ignore = true),
            @Mapping(target = "appKey", ignore = true),
            @Mapping(target = "appSecret", ignore = true),
            @Mapping(target = "status", constant = "1"),
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "updateTime", ignore = true),
    })
    DelayMessageApp toDelayMessageApp(SaveAppKeyReq req);

    SaveAppKeyResp toSaveAppKeyResp(DelayMessageApp req);

    QueryAppKeyInfo toQueryAppKeyResp(DelayMessageAppDto dto);

    List<QueryAppKeyInfo> toQueryAppKeyRespList(List<DelayMessageAppDto> dto);

    QueryAppTopicStatStoreReq toQueryAppTopicStatStoreReq(QueryAppTopicStatReq req);

    QueryDelayMessageSnapshotReq toQueryDelayMessageSnapshotReq(QueryServerLoadReq req);
}
