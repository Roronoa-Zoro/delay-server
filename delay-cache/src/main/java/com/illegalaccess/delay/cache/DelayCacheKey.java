package com.illegalaccess.delay.cache;

import com.google.common.base.Joiner;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * generating cache key using this class
 * 生成缓存key的工具类
 * @author Jimmy Li
 */
@Data
@Service
public class DelayCacheKey {

    @Autowired
    private DelayCacheProperties delayCacheProperties;

    private final String PREFIX = "delay:cache";

    /**
     * 存储appkey metainfo的缓存key
     * @param appKey
     * @return
     */
    public String createCacheKey4AppKey(String appKey) {
        return Joiner.on(":").join(PREFIX, appKey, delayCacheProperties.getCacheVersion());
    }

    /**
     * 消息总量的缓存key
     * @return
     */
    public String createTotalMsgCntKey() {
        return Joiner.on(":").join(PREFIX, "totalMsgCnt", delayCacheProperties.getCacheVersion());
    }

    /**
     * appkey+topic维度消息数量的缓存key
     * key是appkey维度，field的topic，value是数量
     * @param hash
     * @return
     */
    public String createAppKeyAndTopicCntKey(String hash) {
        return Joiner.on(":").join(PREFIX, "appCnt", hash, delayCacheProperties.getCacheVersion());
    }

    /**
     * 初始化内部延时消息使用的key
     * @return
     */
    public String createKey4InitInnerTopic() {
        return Joiner.on(":").join(PREFIX, "initInnerTopic", delayCacheProperties.getCacheVersion());
    }

    /**
     * 非特殊key的过期时间，单位天
     * @return
     */
    public long getDefaultKeyTtlWithDayUnit() {
        return 3650L;
    }

    /**
     * 非特殊key的过期时间，单位小时
     * @return
     */
    public long getDefaultKeyTtlWithHour() {
        return 12L;
    }

}
