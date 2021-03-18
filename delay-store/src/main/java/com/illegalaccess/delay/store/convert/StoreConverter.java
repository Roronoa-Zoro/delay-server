package com.illegalaccess.delay.store.convert;

import com.illegalaccess.delay.client.dto.DelayMessageReq;
import com.illegalaccess.delay.store.dto.DelayMessageAppDto;
import com.illegalaccess.delay.store.dto.DelayMessageDto;
import com.illegalaccess.delay.store.dto.DelayMessageTopicDto;
import com.illegalaccess.delay.store.entity.DelayMessage;
import com.illegalaccess.delay.store.entity.DelayMessageApp;
import com.illegalaccess.delay.store.entity.DelayMessageTopic;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

/**
 * convert income request to db entity and vice versa
 *
 * @author Jimmy Li
 * @date 2021-02-03 17:03
 */
@Mapper(componentModel = "spring")
public interface StoreConverter {

    @Mappings(value = {
            @Mapping(target = "msgId", ignore = true),
            @Mapping(target = "status", constant = "1"),
            @Mapping(target = "modifyTime", ignore = true),
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "msgContent", source = "message"),
            @Mapping(target = "repeatType", constant = "0")
    })
    DelayMessage toMessageEntity(DelayMessageReq delayMessageReq);

    DelayMessageDto toDelayMessageDto(DelayMessage delayMessage);

    List<DelayMessageDto> toDelayMessageDtoList(List<DelayMessage> delayMessage);

    DelayMessageAppDto toDelayMessageAppDto(DelayMessageApp delayMessageApp);
    List<DelayMessageAppDto> toDelayMessageAppDtoList(List<DelayMessageApp> delayMessageApp);

    DelayMessageTopicDto toDelayMessageTopicDto(DelayMessageTopic delayMessageTopic);

    List<DelayMessageTopicDto> toDelayMessageTopicDtoList(List<DelayMessageTopic> delayMessageTopic);


}
