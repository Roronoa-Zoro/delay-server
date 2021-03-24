package com.illegalaccess.delay.protocol.rebalance;

import com.illegalaccess.delay.protocol.support.HostInfo;

import java.util.List;
import java.util.Map;

/**
 * 对槽进行重平衡的操作接口
 * @author Jimmy Li
 * @date 2021-03-04 10:26
 */
public interface SlotRebalance {

//    Map</* slot */Integer, HostInfo> rebalanceSlot(List<Integer> allSLot, List<String> allServerIp, String currentIp);

    /**
     * 结果的key是机器信息，value是它所拥有的槽的数量列表
     * @param allSLot
     * @param allServerIp
     * @param currentIp
     * @return
     */
    Map<HostInfo, List<Integer>> rebalanceSlot(List<Integer> allSLot, List<String> allServerIp, String currentIp);
}
