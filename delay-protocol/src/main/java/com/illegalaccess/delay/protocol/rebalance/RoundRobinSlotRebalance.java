package com.illegalaccess.delay.protocol.rebalance;

import com.illegalaccess.delay.protocol.support.HostInfo;
import com.illegalaccess.delay.protocol.support.ResourceInfo;
import com.illegalaccess.delay.toolkit.Constants;

import java.util.ArrayList;
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


    public Map<Integer, HostInfo> rebalanceSlot2(List<Integer> allSLot, List<String> allServerIp, String currentIp) {

        Integer selfIdx = -1;
        Integer ipSize = allServerIp.size();

        for (String ip : allServerIp) {
            selfIdx++;
            if (currentIp.equals(ip)) {
                break;
            }
        }

        /*
          机器数量比槽数量多，此机器无法分配到槽
         */
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

    @Override
    public Map<HostInfo, List<Integer>> rebalanceSlot(List<Integer> allSLot, List<String> allServerIp, String currentIp) {
        Map<HostInfo, List<Integer>> data = new HashMap<>(allServerIp.size());
        Integer ipSize = allServerIp.size();
        int slotSize = allSLot.size();

        for (int i = 0; i < allServerIp.size(); i++) {
            /*
              机器数量比槽数量多，此机器无法分配到槽
             */
            if (i >= slotSize) {
                break;
            }

            for (int j = i; j < slotSize; j += ipSize) {
                String[] host = allServerIp.get(i).split(Constants.colon);
                List<Integer> slot = data.computeIfAbsent(new HostInfo(host[0], Integer.parseInt(host[1])), key -> new ArrayList<>());
                slot.add(allSLot.get(j));
            }
        }
        return data;
    }


}
