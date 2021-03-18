package com.illegalaccess.delay.ui.client.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UpdateTopicReq implements Serializable {

    private static final long serialVersionUID = -1L;

    private String appKey;

    private String topic;

    private Byte status;
}
