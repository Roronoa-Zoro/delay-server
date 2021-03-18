package com.illegalaccess.delay.protocol;

import com.illegalaccess.delay.toolkit.IPUtils;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 资源信息
 * @author Jimmy Li
 * @date 2021-03-04 10:26
 */
@Data
public class ResourceInfo implements Serializable {
    /**
     * 本机信息
     */
    private static HostInfo self;

    /**
     * 分配给本机处理的槽
     */
    private static volatile int[] assignedSlot;

    private static final AtomicInteger slotIdx = new AtomicInteger(0);

    /**
     * 所有槽的信息，下标从1开始，标识每个槽属于哪个host
     * 当删除发送的延时消息时，请求达到一台机器，如果消息没在当前机器上，则根据此信息，可以O(1)获取到应该处理该消息的机器，然后调用该机器的接口删除消息
     */
    private static Map</* 槽 id */Integer, HostInfo> allSlotHostInfo;
//    private HostInfo[] allSlotHostInfo;

    public static final void registerSelfInfo() {
        String hostIp = IPUtils.getHostIp();
        self = new HostInfo();
        self.setHostIp(hostIp);
        self.setPort(18080);
    }

    public static final boolean assignSlot(int[] slot) {
        assignedSlot = slot;
        return true;
    }

    public static final int[] getAssignedSlot() {
        return assignedSlot;
    }

    public static final int getSlot() {
        Integer idx = slotIdx.incrementAndGet();
        return assignedSlot[idx % assignedSlot.length];
    }


    public static final void registerAllSlot() {
        // todo
    }

    public static HostInfo getSelf() {
        return self;
    }
}
