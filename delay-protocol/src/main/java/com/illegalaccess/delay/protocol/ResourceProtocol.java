package com.illegalaccess.delay.protocol;

import com.illegalaccess.delay.protocol.enums.RegisterStatusEnum;

/**
 * 资源协议接口
 * @date 2021-03-04 10:27
 * @author Jimmy Li
 */
public interface ResourceProtocol {

    /**
     * 注册
     *
     * @return
     */
    RegisterStatusEnum register();

    /**
     * 取消注册
     *
     * @return
     */
    RegisterStatusEnum unregister();

    /**
     * 监控注册列表
     */
    void monitorRegisterStatus();

    /**
     * 数据重平衡
     */
    void rebalance();

    /**
     * 分配资源
     *
     * @param number
     * @return
     */
    boolean arrangeResource(int number);

    /**
     * 扩容资源
     *
     * @param number
     * @return
     */
    boolean enlargeResource(int number);

    /**
     * 获取所有的槽
     * @return
     */
    int[] getAllSlot();
}
