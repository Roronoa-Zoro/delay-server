package com.illegalaccess.delay.protocol.constant;

import com.illegalaccess.delay.protocol.ResourceInfo;

import java.util.StringJoiner;

/**
 * @author Jimmy Li
 * @date 2021-01-28 10:20
 */
public abstract class ProtocolConstant {

    /**
     * 根路径
     */
    public static String Root_Path = "/delay";

    /**
     * 所有槽的路径
     */
    public static String ALL_SLOT_PATH = "/delay/slot/all-slot/";

    /**
     * 已分配的槽 路径
     */
    public static String ASSIGNED_SLOT_PATH = "/delay/slot/assigned-slot/";

    /**
     * 机器注册的节点
     */
    public static String REGISTER_SERVER_PATH = "/delay/servers/";

    public static String buildRegisterNode(String ip) {
        return new StringJoiner("", REGISTER_SERVER_PATH, ip).toString();
    }

    public static String buildAssignSlotPath() {
        return new StringJoiner("", ASSIGNED_SLOT_PATH, ResourceInfo.getSelf().getHostIp()).toString();
    }

}
