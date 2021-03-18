package com.illegalaccess.delay.protocol.rebalance;

import com.illegalaccess.delay.protocol.HostInfo;

import java.util.List;
import java.util.Map;

/**
 * 对槽进行重平衡的操作接口
 * @author Jimmy Li
 * @date 2021-03-04 10:26
 */
public interface SlotRebalance {

    Map</* slot */Integer, HostInfo> rebalanceSlot(List<Integer> allSLot, List<String> allServerIp, String currentIp);
}
