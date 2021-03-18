package com.illegalaccess.delay.store.cassandra;

import com.illegalaccess.delay.client.dto.DelayMessageReq;
import com.illegalaccess.delay.store.cassandra.entity.DelayMessage;
import com.illegalaccess.delay.store.cassandra.entity.DelayMessageApp;
import com.illegalaccess.delay.store.dto.DelayMessageAppDto;
import com.illegalaccess.delay.store.dto.DelayMessageDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CassandraStoreConverter {

    @Mappings(value = {
            @Mapping(target = "msgId", ignore = true),
            @Mapping(target = "execTime", ignore = true),
            @Mapping(target = "status", constant = "1"),
            @Mapping(target = "modifyTime", ignore = true),
//            @Mapping(target = "id", ignore = true),
            @Mapping(target = "msgContent", source = "message")
    })
    DelayMessage toMessageEntity(DelayMessageReq delayMessageReq);

    DelayMessageDto toDelayMessageDto(DelayMessage delayMessage);

    List<DelayMessageDto> toDelayMessageDtoList(List<DelayMessage> delayMessage);


    DelayMessageAppDto toDelayMessageAppDto(DelayMessageApp delayMessageApp);
}
