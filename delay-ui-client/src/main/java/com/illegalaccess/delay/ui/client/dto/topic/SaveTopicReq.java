package com.illegalaccess.delay.ui.client.dto.topic;

import com.illegalaccess.delay.toolkit.enums.MQTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaveTopicReq implements Serializable {

    private static final long serialVersionUID = -1L;

    private String appKey;

    private MQTypeEnum mqType;

    private String topic;

    private String creator;

    private String creatorOrg;
}
