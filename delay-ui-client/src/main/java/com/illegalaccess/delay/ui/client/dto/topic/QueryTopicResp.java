package com.illegalaccess.delay.ui.client.dto;

import com.illegalaccess.delay.toolkit.dto.PageResponse;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class QueryTopicResp extends PageResponse {

    private List<QueryTopicInfo> data;
}
