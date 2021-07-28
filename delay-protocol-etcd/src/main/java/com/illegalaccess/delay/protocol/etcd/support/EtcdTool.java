package com.illegalaccess.delay.protocol.etcd.support;

import com.illegalaccess.delay.protocol.support.HostInfo;
import com.illegalaccess.delay.protocol.support.ProtocolProperties;
import com.illegalaccess.delay.protocol.callback.MonitorCallback;
import com.illegalaccess.delay.protocol.constant.ProtocolConstant;
import com.illegalaccess.delay.protocol.enums.ResourceChangeTypeEnum;
import com.illegalaccess.delay.toolkit.Constants;
import com.illegalaccess.delay.toolkit.dto.Pairs;
import com.illegalaccess.delay.toolkit.function.ThrowingSupplier;
import com.illegalaccess.delay.toolkit.json.JsonTool;
import io.etcd.jetcd.ByteSequence;
import io.etcd.jetcd.Client;
import io.etcd.jetcd.KeyValue;
import io.etcd.jetcd.kv.GetResponse;
import io.etcd.jetcd.kv.PutResponse;
import io.etcd.jetcd.lease.LeaseGrantResponse;
import io.etcd.jetcd.lease.LeaseKeepAliveResponse;
import io.etcd.jetcd.options.GetOption;
import io.etcd.jetcd.options.PutOption;
import io.etcd.jetcd.options.WatchOption;
import io.etcd.jetcd.watch.WatchEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * 聚合操作etcd的接口
 * @author Jimmy Li
 * @date 2021-01-28 21:15
 */
@Component
public class EtcdTool {

    private final Logger logger = LoggerFactory.getLogger(EtcdTool.class);

    @Autowired
    private Client client;
    @Autowired
    private ProtocolProperties protocolProperties;
    private MonitorCallback monitorCallback;

    /**
     * 注册本机IP，创建租约
     *
     * @return
     */
    public boolean register(HostInfo hostInfo) {
        String hostIp = hostInfo.getHostIp();
        String hostValue = hostIp + Constants.colon + hostInfo.getPort();
        String hostKey = ProtocolConstant.buildRegisterNode(hostIp);

        GetResponse getResp = getQuietly(() -> client.getKVClient().get(toByteSequence(hostKey)).get());
        if (getResp.getCount() > 0) {
            logger.info("hostIp:{} is registered", hostIp);
        }

        /**
         * todo
         * 这里需要保存lease id到redis，当服务异常挂掉，被操作系统再次拉起后，如果时间很短，则可以保存lease id不变，不必重新触发 rebalance
         * 如果挂了，然后被OS快速拉起，此时leaseId未过期，虽然可以继续续期之前的lease，但是这台机器的槽怎么获取回来？？？？
         * answer：
         * 方案1：把槽信息和leaseId一起写入磁盘，如果服务恢复后leaseId没有过期，则触发一个recover的操作，把槽信息分配到本机，开始提供服务
         * 方案2：如果服务恢复后leaseId没有过期，则触发一个recover的操作，从远程服务获取所有槽和机器，执行rebalance算法，得到本机的槽信息，分配到本机，开始提供服务
         * 结论：使用方案2，数据的准确性更高
         */
        Long leaseId = EtcdLeaseTool.getLeaseExpireTime(protocolProperties.getLeasePath());
        long ttl = renew(leaseId);
        if (ttl > 0) {
            logger.info("old lease is not expire, keep use it");
            EtcdResourceInfo.updateLease(leaseId);

            // 触发一个recover事件
            monitorCallback.callback(ResourceChangeTypeEnum.Server_List_Change);
            return true;
        }

        LeaseGrantResponse grantResponse = getQuietly(() -> client.getLeaseClient().grant(protocolProperties.getTtl(), protocolProperties.getTimeout(), TimeUnit.SECONDS).get());
        logger.info("will lease id:{}", grantResponse.getID());
        EtcdResourceInfo.updateLease(grantResponse.getID());
        CompletableFuture<PutResponse> putFuture = client.getKVClient().put(toByteSequence(hostKey),
                toByteSequence(hostValue),
                PutOption.newBuilder().withLeaseId(grantResponse.getID()).build()
        );

        PutResponse putResponse = getQuietly(() -> putFuture.get());
        logger.info("put current machine:{} successfully:{}", hostKey, putResponse);
        return true;
    }

    /**
     *
     * @return
     */
    public boolean unregister() {
        logger.info("will del from server list and assigned slot path for:{}", EtcdResourceInfo.getLeaseId());
        client.getLeaseClient().revoke(EtcdResourceInfo.getLeaseId());
        return true;
    }

    /**
     * 监听槽节点 和 机器列表节点
     *
     * @return
     */
    public boolean monitor(MonitorCallback monitorCallback) {
        if (this.monitorCallback == null) {
            this.monitorCallback = monitorCallback;
        }
        client.getWatchClient().watch(
                ByteSequence.from(ProtocolConstant.ALL_SLOT_PATH, Charset.forName("utf-8")),
//                WatchOption.newBuilder().withPrefix(ByteSequence.from(ProtocolConstant.ALL_SLOT_PATH, Charset.forName("utf-8"))).build(),
                (watchResponse -> {
                    logger.info("slot list is changed{}", JsonTool.toJsonString(watchResponse));
                    List<WatchEvent> watchEventList = watchResponse.getEvents();
                    WatchEvent watchEvent = watchEventList.get(0);
                    monitorCallback.callback(ResourceChangeTypeEnum.Slot_Change);
                })
        );

        client.getWatchClient().watch(
                ByteSequence.from(ProtocolConstant.REGISTER_SERVER_PATH, Charset.forName("utf-8")),
                WatchOption.newBuilder().withNoPut(false).withNoDelete(false).withPrefix(ByteSequence.from(ProtocolConstant.REGISTER_SERVER_PATH, Charset.forName("utf-8"))).build(),
                (watchResponse -> {
                    logger.info("server list is changed{}", JsonTool.toJsonString(watchResponse));
                    monitorCallback.callback(ResourceChangeTypeEnum.Server_List_Change);
                })
        );

        return true;
    }

    /**
     *
     * @param leaseId
     * @return ttl
     */
    public long renew(Long leaseId) {
        CompletableFuture<LeaseKeepAliveResponse> renew = client.getLeaseClient().keepAliveOnce(leaseId);
        LeaseKeepAliveResponse response = getQuietly(() -> renew.get());
        EtcdLeaseTool.writeLeaseInfo(protocolProperties.getLeasePath(), leaseId);
        return response.getTTL();
    }

    /**
     * put value method
     * @param key
     * @param value
     * @return
     */
    public boolean putValue(String key, String value) {
        PutResponse putResponse = asyncPutValue(key, value, null);
        logger.info("put resp:{}", JsonTool.toJsonString(putResponse));
        return true;
    }

    /**
     * put value with lease
     * @param key
     * @param value
     * @return
     */
    public boolean putValueWithLease(String key, String value) {
        PutResponse putResponse = asyncPutValue(key, value, PutOption.newBuilder().withLeaseId(EtcdResourceInfo.getLeaseId()).build());
        logger.info("put resp:{}", JsonTool.toJsonString(putResponse));
        return true;
    }

    /**
     * async put value
     * @param key
     * @param value
     * @param putOption
     * @return
     */
    private PutResponse asyncPutValue(String key, String value, PutOption putOption) {
        if (putOption == null) {
            CompletableFuture<PutResponse> putFuture = client.getKVClient().put(toByteSequence(key), toByteSequence(value));
            return getQuietly(() -> putFuture.get());
        }

        CompletableFuture<PutResponse> putFuture = client.getKVClient().put(toByteSequence(key), toByteSequence(value), putOption);
        return getQuietly(() -> putFuture.get());
    }

    /**
     * get key and value for a specific key
     *
     * @param key
     * @return
     */
    public Pairs getKeyValue(String key) {
        logger.info("get value for key:{}", key);
        ByteSequence keyByte = toByteSequence(key);
        GetResponse getResp = getQuietly(() -> client.getKVClient().get(keyByte).get());

        List<KeyValue> keyValues = getResp.getKvs();
        if (CollectionUtils.isEmpty(keyValues)) {
            return null;
        }

        KeyValue keyValue = keyValues.get(0);
        Pairs paris = new Pairs();
        paris.setKey(keyValue.getKey().toString(Charset.forName("utf-8")));
        paris.setValue(keyValue.getValue().toString(Charset.forName("utf-8")));
        return paris;
    }

    /**
     * get key and value list for key prefix
     *
     * @param key
     * @return
     */
    public List<Pairs> getKeyWithPrefix(String key) {
        logger.info("get all sub for key:{}", key);
        ByteSequence keyByte = toByteSequence(key);
        GetResponse getResp = getQuietly(() -> client.getKVClient().get(keyByte, GetOption.newBuilder().withPrefix(keyByte).build()).get());

        List<KeyValue> keyValues = getResp.getKvs();
        if (CollectionUtils.isEmpty(keyValues)) {
            return new ArrayList<>(0);
        }

        List<Pairs> parisList = new ArrayList<>();
        for (KeyValue keyValue : keyValues) {
            Pairs paris = new Pairs();
            paris.setKey(keyValue.getKey().toString(Charset.forName("utf-8")));
            paris.setValue(keyValue.getValue().toString(Charset.forName("utf-8")));
            parisList.add(paris);
        }
        return parisList;
    }

    /**
     * 统一处理异常的supplier
     *
     * @param supplier
     * @param <T>
     * @return
     */
    private <T> T getQuietly(ThrowingSupplier<T> supplier) {
        try {
            return supplier.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * convert byte to string
     * @param byteSequence
     * @return
     */
    public String byte2String(ByteSequence byteSequence) {
        return byteSequence.toString(Charset.forName("utf-8"));
    }

    /**
     * 转换成ByteSequence
     *
     * @param value
     * @return
     */
    private ByteSequence toByteSequence(String value) {
        return ByteSequence.from(value, Charset.forName("utf-8"));
    }

}
