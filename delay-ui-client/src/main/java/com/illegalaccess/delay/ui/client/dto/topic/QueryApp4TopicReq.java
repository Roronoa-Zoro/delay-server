package com.illegalaccess.delay.ui.client.dto.topic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 根据appkey + topic查询是否存在数据
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QueryApp4TopicReq implements Serializable {

    private String appKey;

    private String topic;
}
