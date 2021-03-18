package com.illegalaccess.delay.protocol.rebalance;

import com.illegalaccess.delay.protocol.HostInfo;
import com.illegalaccess.delay.protocol.ResourceInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * slot:    1,2,3,4,5,6,7,8
 * servers: 127.0.0.1, 127.0.0.2, 127.0.0.3
 * <p>
 * result:
 * slot:     1,4,7
 * servers:  127.0.0.1
 * <p>
 * slot:     2,5,8
 * servers:  127.0.0.2
 * <p>
 * slot:     3,6
 * servers:  127.0.0.3
 *
 * @date 2021-03-04 10:26
 * @author Jimmy Li
 */
public class RoundRobinSlotRebalance implements SlotRebalance {

    @Override
    public Map<Integer, HostInfo> rebalanceSlot(List<Integer> allSLot, List<String> allServerIp, String currentIp) {

        Integer selfIdx = -1;
        Integer ipSize = allServerIp.size();

        for (String ip : allServerIp) {
            selfIdx++;
            if (currentIp.equals(ip)) {
                break;
            }
        }

        if (selfIdx > (allSLot.size() - 1)) {
            return new HashMap<>(0);
        }

        Map<Integer, HostInfo> assignedSlot = new HashMap<>();
        for (int i = selfIdx; i < allSLot.size(); i += ipSize) {
            Integer slot = allSLot.get(i);
            assignedSlot.put(slot, ResourceInfo.getSelf());
        }
        return assignedSlot;
    }
}
