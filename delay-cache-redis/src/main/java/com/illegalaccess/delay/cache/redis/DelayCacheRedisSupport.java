package com.illegalaccess.delay.cache.redis;

import com.illegalaccess.delay.cache.DelayCacheSupport;
import com.illegalaccess.delay.toolkit.json.JsonTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * 基于redis实现的缓存
 * @author Jimmy Li
 */
@Slf4j
@Component
public class DelayCacheRedisSupport implements DelayCacheSupport {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * get value from cache, and convert to object
     * @param key
     * @param clz
     * @param <T>
     * @return
     */
    @Override
    public <T> T getValueFromCache(String key, Class<T> clz) {
        String val = stringRedisTemplate.opsForValue().get(key);
        return JsonTool.parseObject(val, clz);
    }

    /**
     * get value from cache, if not exists, will get from function method, then return
     * @param key
     * @param clz
     * @param ttl
     * @param ttlUnit
     * @param function
     * @param t
     * @param <T>
     * @param <R>
     * @return
     */
    @Override
    public <T, R> R getValueFromCache(String key, Class<R> clz, Long ttl, TimeUnit ttlUnit, Function<T, R> function, T t) {
        String val = stringRedisTemplate.opsForValue().get(key);
        if (!StringUtils.isEmpty(val)) {
            return JsonTool.parseObject(val, clz);
        }

        R res = function.apply(t);
        if (res == null) {
            log.info("get data null from function");
            return null;
        }
        log.info("get from function and put to cache");
        stringRedisTemplate.opsForValue().set(key, JsonTool.toJsonString(res), ttl, ttlUnit);
        return res;
    }

    /**
     * put data to cache with ttl
     * @param key
     * @param value
     * @param ttl
     * @param ttlUnit
     */
    @Override
    public void set(String key, Object value, Long ttl, TimeUnit ttlUnit) {
        stringRedisTemplate.opsForValue().set(key, JsonTool.toJsonString(value), ttl, ttlUnit);
    }

    @Override
    public boolean setIfAbsent(String key, Object value, Long ttl, TimeUnit ttlUnit) {
        return stringRedisTemplate.opsForValue().setIfAbsent(key, JsonTool.toJsonString(value), ttl, ttlUnit);
    }

    @Override
    public void incr(String key, long val) {
        stringRedisTemplate.opsForValue().increment(key, val);
    }

    @Override
    public void incrHashField(String hash, String field, long val) {
        stringRedisTemplate.opsForHash().increment(hash, field, val);
    }

    @Override
    public Map<Object, Object> entries(String hash) {
        return stringRedisTemplate.opsForHash().entries(hash);
    }

    @Override
    public boolean del(String key) {
        return stringRedisTemplate.delete(key);
    }
}
