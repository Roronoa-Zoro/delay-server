package com.illegalaccess.delay.protocol.etcd;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.primitives.Ints;
import com.illegalaccess.delay.protocol.HostInfo;
import com.illegalaccess.delay.protocol.ProtocolProperties;
import com.illegalaccess.delay.protocol.ResourceInfo;
import com.illegalaccess.delay.protocol.ResourceProtocol;
import com.illegalaccess.delay.protocol.constant.ProtocolConstant;
import com.illegalaccess.delay.protocol.enums.RegisterStatusEnum;
import com.illegalaccess.delay.protocol.etcd.task.LeaseTask;
import com.illegalaccess.delay.protocol.rebalance.RoundRobinSlotRebalance;
import com.illegalaccess.delay.protocol.rebalance.SlotRebalance;
import com.illegalaccess.delay.toolkit.dto.Pairs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Jimmy Li
 * @date 2021-01-27 20:23
 */
@Slf4j
public class EtcdResourceProtocol implements ResourceProtocol {

    @Autowired
    private EtcdTool etcdTool;
    @Autowired
    private ProtocolProperties protocolProperties;

    @Resource(name = "leaseScheduledExecutorService")
    private ScheduledExecutorService scheduledExecutorService;

    private SlotRebalance slotRebalance = new RoundRobinSlotRebalance();

    @Override
    public RegisterStatusEnum register() {
        // 注册本机
        boolean res = etcdTool.register();
        log.info("register self res:{}", res);
        // 自动续约
        scheduledExecutorService.scheduleAtFixedRate(new LeaseTask(etcdTool), 0, protocolProperties.getTtl() / 3, TimeUnit.SECONDS);
        return RegisterStatusEnum.Register_As_Member;
    }

    @Override
    public RegisterStatusEnum unregister() {
        return null;
    }

    @Override
    public void monitorRegisterStatus() {
        etcdTool.monitor();
    }

    @Override
    public void rebalance() {
        log.info("will do re-balance");
        List<Pairs> allServerList = etcdTool.getKeyWithPrefix(ProtocolConstant.REGISTER_SERVER_PATH);
        Pairs allSlotPairs = etcdTool.getKeyValue(ProtocolConstant.ALL_SLOT_PATH);
        if (allSlotPairs == null) {
            ResourceInfo.assignSlot(new int[]{1});
        }

        String allSlot = allSlotPairs.getValue();

        int[] slotArray = convertSlot(allSlot);
        List<Integer> slotListInt = new ArrayList<>(slotArray.length);
        Arrays.stream(slotArray).forEach(ic -> slotListInt.add(ic));

        List<String> allServerIp = new ArrayList<>(allServerList.size());
        for (Pairs pairs : allServerList) {
            allServerIp.add(pairs.getKey());
        }
        Map<Integer, HostInfo> rebalanceRes = slotRebalance.rebalanceSlot(slotListInt, allServerIp, EtcdResourceInfo.getSelf().getHostIp());
        Set<Integer> slotSet = rebalanceRes.keySet();
        ResourceInfo.assignSlot(Ints.toArray(slotSet));


        String slot4Join = Joiner.on(",").join(slotSet);
        log.info("current machine assigned slot:{}", slot4Join);
        boolean put = etcdTool.putValueWithLease(ProtocolConstant.buildAssignSlotPath(), slot4Join);
        log.info("put value res:{}", put);
    }

    @Override
    public boolean arrangeResource(int number) {
        return false;
    }

    @Override
    public boolean enlargeResource(int number) {
        return false;
    }

    /**
     * convert slot to array
     * @param allSlot
     * @return
     */
    private int[] convertSlot(String allSlot) {
        List<String> slotList = Splitter.on(",").splitToList(allSlot);
//        List<Integer> slotListInt = new ArrayList<>(slotList.size());
        int[] resp = new int[slotList.size()];
        for (int i = 0; i < slotList.size(); i++) {
            resp[i] = Integer.valueOf(slotList.get(i));
        }

        return resp;
    }
    @Override
    public int[] getAllSlot() {
        Pairs allSlotPairs = etcdTool.getKeyValue(ProtocolConstant.ALL_SLOT_PATH);
        if (allSlotPairs == null) {
            return new int[]{1};
        }

        return convertSlot(allSlotPairs.getValue());
    }

}
