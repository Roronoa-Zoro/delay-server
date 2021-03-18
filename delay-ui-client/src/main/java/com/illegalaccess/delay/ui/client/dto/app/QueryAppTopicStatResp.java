package com.illegalaccess.delay.ui.client.dto.app;

import com.illegalaccess.delay.ui.client.dto.TrendInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QueryAppTopicStatResp implements Serializable {

    private static final long serialVersionUID = -1L;

    private String appKey;

    private TrendInfo trendInfo;
}
