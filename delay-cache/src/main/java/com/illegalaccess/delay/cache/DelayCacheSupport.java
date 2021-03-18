package com.illegalaccess.delay.cache;


import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
/**
 * cache service support
 * 缓存接口
 * @author Jimmy Li
 * @date 2021-03-01 17:06
 */
public interface DelayCacheSupport {

    /**
     * get value from cache, and convert to object
     * @param key
     * @param clz
     * @param <T>
     * @return
     */
    <T> T getValueFromCache(String key, Class<T> clz);

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
    <T, R> R getValueFromCache(String key, Class<R> clz, Long ttl, TimeUnit ttlUnit, Function<T, R> function, T t);

    /**
     * put data to cache with ttl
     * @param key
     * @param value
     * @param ttl
     * @param ttlUnit
     */
    void set(String key, Object value, Long ttl, TimeUnit ttlUnit);

    boolean setIfAbsent(String key, Object value, Long ttl, TimeUnit ttlUnit);

    void incr(String key, long val);

    void incrHashField(String hash, String field, long val);

    Map<Object, Object> entries(String hash);

    boolean del(String key);
}
