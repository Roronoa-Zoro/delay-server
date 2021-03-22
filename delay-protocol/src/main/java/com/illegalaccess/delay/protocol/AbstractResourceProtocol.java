package com.illegalaccess.delay.protocol;

import com.google.common.base.Joiner;
import com.google.common.primitives.Ints;
import com.illegalaccess.delay.protocol.constant.ProtocolConstant;
import com.illegalaccess.delay.protocol.rebalance.RoundRobinSlotRebalance;
import com.illegalaccess.delay.protocol.rebalance.SlotRebalance;
import com.illegalaccess.delay.protocol.support.HostInfo;
import com.illegalaccess.delay.protocol.enums.RegisterStatusEnum;
import com.illegalaccess.delay.protocol.support.RebalanceParticipant;
import com.illegalaccess.delay.protocol.support.ResourceInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.Map;
@Slf4j
public abstract class AbstractResourceProtocol implements ResourceProtocol {

    private SlotRebalance slotRebalance = new RoundRobinSlotRebalance();

    @Value("${server.port}")
    private int webPort;

    /**
     * 注册本机本地信息
     * @return
     */
    protected HostInfo registerLocal() {
        ResourceInfo.registerSelfInfo(webPort);
        return ResourceInfo.getSelf();
    }

    /**
     * 注册到远程
     * @param hostInfo
     * @return
     */
    protected abstract RegisterStatusEnum registerRemote(HostInfo hostInfo);

    @Override
    public RegisterStatusEnum register() {
        HostInfo hostInfo = registerLocal();
        registerRemote(hostInfo);
        return null;
    }

    /**
     * 准备重平衡参与者
     * 需要提供所有IP和所有槽
     * @return
     */
    protected abstract RebalanceParticipant prepareParticipant();

    @Override
    public void rebalance() {
        RebalanceParticipant rebalanceParticipant = prepareParticipant();
        log.info("rebalance participant is ready");

        // 执行重平衡算法
        Map<HostInfo, List<Integer>> rebalanceRes = slotRebalance.rebalanceSlot(rebalanceParticipant.getSlotListInt(), rebalanceParticipant.getAllServerIp(), ResourceInfo.getSelf().getHostIp());

        HostInfo self = ResourceInfo.getSelf();

        List<Integer> selfSlot = rebalanceRes.get(self);
        // 注册本机要处理的槽
        ResourceInfo.assignSlot(Ints.toArray(selfSlot));
        log.info("current machine assigned slot:{}", selfSlot);

        // 注册槽和机器的对应关系
        ResourceInfo.registerAllSlot(rebalanceRes);
        log.info("all slot info is updated in local");

        updateRemoteAssignedSlot(selfSlot);
        log.info("new assigned slot is updated in remote");
    }

    /**
     * 更新远端的给本机分配的槽
     * @param selfSlot
     */
    protected abstract void updateRemoteAssignedSlot(List<Integer> selfSlot);
}
