package com.illegalaccess.delay.protocol.zk;

import com.google.common.base.Joiner;
import com.illegalaccess.delay.protocol.AbstractResourceProtocol;
import com.illegalaccess.delay.protocol.callback.MonitorCallback;
import com.illegalaccess.delay.protocol.constant.ProtocolConstant;
import com.illegalaccess.delay.protocol.enums.RegisterStatusEnum;
import com.illegalaccess.delay.protocol.enums.ResourceChangeTypeEnum;
import com.illegalaccess.delay.protocol.support.HostInfo;
import com.illegalaccess.delay.protocol.support.RebalanceParticipant;
import com.illegalaccess.delay.protocol.support.ResourceInfo;
import com.illegalaccess.delay.toolkit.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class ZookeeperResourceProtocol extends AbstractResourceProtocol {

    @Autowired
    private CuratorFramework curatorFramework;

    @Override
    protected RegisterStatusEnum registerRemote(HostInfo hostInfo) {
        String hostValue = hostInfo.getHostIp() + Constants.colon + hostInfo.getPort();
        try {
            String path = curatorFramework.create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                    .forPath(ProtocolConstant.buildRegisterNode(hostInfo.getHostIp()), hostValue.getBytes());
            log.info("register host info resp:{}", path);
        } catch (Exception e) {
            log.error("register host info to remote failure", e);
            //todo
        }
        return RegisterStatusEnum.Register_As_Member;
    }

    @Override
    protected RebalanceParticipant prepareParticipant() {
        try {
            List<String> allServer = curatorFramework.getChildren().forPath(ProtocolConstant.REGISTER_SERVER_PATH);
            byte[] data = curatorFramework.getData().forPath(ProtocolConstant.ALL_SLOT_PATH);
            String slotStr = new String(data);
            String[] slots = slotStr.split(Constants.comma);
            List<Integer> slotList = new ArrayList<>(slots.length);
            Arrays.stream(slots).forEach(slot -> slotList.add(Integer.valueOf(slot)));
            return RebalanceParticipant.builder().allServerIp(allServer).slotListInt(slotList).build();
        } catch (Exception e) {
            log.info("prepare rebalance participant fail", e);
        }
        return RebalanceParticipant.builder().allServerIp(new ArrayList<>(0)).slotListInt(new ArrayList<>(0)).build();
    }

    @Override
    protected void updateRemoteAssignedSlot(List<Integer> selfSlot) {
        try {
            String slot4Join = Joiner.on(",").join(selfSlot);
            String slotPath = ProtocolConstant.buildAssignSlotPath();
            Stat stat = curatorFramework.checkExists().forPath(slotPath);
            log.info("check path:{} resp:{}", slotPath, stat);
            if (stat == null) {
                String create = curatorFramework.create()
                        .creatingParentsIfNeeded()
                        .withMode(CreateMode.PERSISTENT_SEQUENTIAL)
                        .forPath(slotPath, slot4Join.getBytes());
                log.info("write slot path data:{} resp:{}", slot4Join, create);
                return;
            }

            stat = curatorFramework.setData().forPath(slotPath, slot4Join.getBytes());
            log.info("update slot path data:{} resp:{}", slot4Join, stat);

        } catch (Exception e) {
            log.error("update assigned slot:{} failure", selfSlot, e);
        }
    }

    @Override
    public RegisterStatusEnum unregister() {
        try {
            curatorFramework.delete().forPath(ProtocolConstant.buildRegisterNode(ResourceInfo.getSelf().getHostIp()));
        } catch (Exception e) {
            log.info("unregister failure", e);
            //todo
        }
        return RegisterStatusEnum.Register_As_Member;
    }

    @Override
    public void monitorResource(MonitorCallback monitorCallback) {
        NodeCache nodeCache = new NodeCache(curatorFramework, ProtocolConstant.ALL_SLOT_PATH);
        //调用start方法开始监听
        try {
            nodeCache.start();
            //添加NodeCacheListener监听器
            nodeCache.getListenable().addListener(() -> {
                log.info("槽变更监听到事件变化，当前数据:{}", new String(nodeCache.getCurrentData().getData()));
                monitorCallback.callback(ResourceChangeTypeEnum.Slot_Change);
            });

            log.info("slot path is monitored");
            PathChildrenCache pathChildrenCache = new PathChildrenCache(curatorFramework, ProtocolConstant.REGISTER_SERVER_PATH, true);
            //调用start方法开始监听 ，设置启动模式为同步加载节点数据
            pathChildrenCache.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);
            //添加监听器
            pathChildrenCache.getListenable().addListener((client, event) -> {
                log.info("所有server节点数据变化,类型:{},路径:{}", event.getType(), event.getData().getPath());
                monitorCallback.callback(ResourceChangeTypeEnum.Server_List_Change);
            });
            log.info("server list path is monitored");
        } catch (Exception e) {
            log.info("monitor remote zk path failure", e);
            throw new RuntimeException("monitor remote zk path failure");
        }

    }

    @Override
    public boolean arrangeResource(int number) {
        return false;
    }

    @Override
    public boolean enlargeResource(int number) {
        return false;
    }

    @Override
    public int[] getAllSlot() {
        try {
            byte[] data = curatorFramework.getData().forPath(ProtocolConstant.ALL_SLOT_PATH);
            String allSlot = new String(data);
            String[] slotStr = allSlot.split(Constants.comma);
            return Arrays.stream(slotStr).mapToInt(Integer::valueOf).toArray();
        } catch (Exception e) {
            log.info("get all slot failure", e);
        }
        return new int[0];
    }
}
