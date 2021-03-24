package com.illegalaccess.delay.protocol.rebalance;

import com.illegalaccess.delay.protocol.support.HostInfo;

import java.util.List;
import java.util.Map;

/**
 * slot:    1,2,3,4,5,6,7,8
 * servers: 127.0.0.1, 127.0.0.2, 127.0.0.3
 * <p>
 * result:
 * slot:     1,2,3
 * servers:  127.0.0.1
 * <p>
 * slot:     4,5,6
 * servers:  127.0.0.2
 * <p>
 * slot:     7,8
 * servers:  127.0.0.3
 *
 * @date 2021-03-04 10:26
 * @author Jimmy Li
 */
public class RangeSlotRebalance implements SlotRebalance {


    @Override
    public Map<HostInfo, List<Integer>> rebalanceSlot(List<Integer> allSLot, List<String> allServerIp, String currentIp) {
        return null;
    }
}
