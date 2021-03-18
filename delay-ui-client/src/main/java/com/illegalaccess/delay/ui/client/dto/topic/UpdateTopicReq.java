package com.illegalaccess.delay.ui.client.dto.topic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateTopicReq implements Serializable {

    private static final long serialVersionUID = -1L;

    private String appKey;

    private String topic;

    private Byte status;
}
