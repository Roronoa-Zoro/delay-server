package com.illegalaccess.delay.protocol.support;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 重平衡参与者信息
 */
@Data
@Setter
@Getter
@Builder
public class RebalanceParticipant {

    /**
     * 所有的机器IP
     */
    private List<String> allServerIp;

    /**
     * 所有的槽
     */
    private List<Integer> slotListInt;
}
