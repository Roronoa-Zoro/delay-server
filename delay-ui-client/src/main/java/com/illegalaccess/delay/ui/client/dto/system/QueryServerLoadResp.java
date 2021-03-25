package com.illegalaccess.delay.ui.client.dto.system;

import com.illegalaccess.delay.ui.client.dto.TrendInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 查询系统负载返回信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QueryServerLoadResp implements Serializable {

    /**
     * 趋势图数据
     */
    private TrendInfo trendInfo;
}
