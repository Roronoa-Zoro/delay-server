package com.illegalaccess.delay.ui.client.dto;

import com.illegalaccess.delay.toolkit.dto.PageRequest;
import lombok.Data;

@Data
public class QueryTopicReq extends PageRequest {

    private static final long serialVersionUID = -1L;

    private String appKey;
}
