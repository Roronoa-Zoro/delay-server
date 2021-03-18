package com.illegalaccess.delay.toolkit;

import com.google.common.base.Joiner;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

/**
 * unique id generator
 * @date 2021-03-04 10:36
 * @author Jimmy Li
 */
public class IdGenerator {

    private static final String PROCESS_ID = UUID.randomUUID().toString().replaceAll("-", "") + ".";
    private static final AtomicLong SUFFIX = new AtomicLong(0);

    private static final AtomicLong LONG_SUFFIX = new AtomicLong(0);
    private static long LONG_PREFIX = System.currentTimeMillis();

    private IdGenerator() {
    }

    /**
     * PROCESS_ID + 单调自增ID
     * <p>
     * 每次服务启动生成唯一的PROCESS_ID，保证所有机器有唯一的前缀标识
     * 然后AtomicLong保证单机内id单调递增且唯一
     * <p>
     * 整体的ID的全局唯一
     */
    public static final String generate() {
        return PROCESS_ID + SUFFIX.incrementAndGet();
    }

    /**
     * generate long id
     * @return
     */
    public static final long longId() {
        String data = Joiner.on("").join(LONG_PREFIX, LONG_SUFFIX.incrementAndGet());
        return Long.parseLong(data);
    }

    public static final String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

}
