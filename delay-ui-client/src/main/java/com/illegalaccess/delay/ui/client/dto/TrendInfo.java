package com.illegalaccess.delay.ui.client.dto;

import com.illegalaccess.delay.toolkit.dto.Pair;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TrendInfo implements Serializable {

    private static final long serialVersionUID = -1L;

    /**
     * x 轴数据
     */
    private List<String> xContent;

    /**
     * 趋势折线数据
     */
    private List<Pair<String, List<Integer>>> data;
}
