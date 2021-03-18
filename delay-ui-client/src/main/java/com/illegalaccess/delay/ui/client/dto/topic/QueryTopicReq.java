package com.illegalaccess.delay.ui.client.dto.topic;

import com.illegalaccess.delay.toolkit.dto.PageRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QueryTopicReq extends PageRequest {

    private static final long serialVersionUID = -1L;

    private String appKey;
}
