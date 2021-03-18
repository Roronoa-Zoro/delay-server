package com.illegalaccess.delay.core.delay;


import com.illegalaccess.delay.toolkit.dto.Pair;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 延时消息统计信息相关
 * @author Jimmy Li
 * @date 2021-03-04 10:36
 */
public class DelayMessageStatSupport {

    /**
     * 处理的消息总量
     */
    private static final AtomicInteger totalCnt = new AtomicInteger(0);

    /**
     * key是 appkey + topic
     * value是数量
     */
    private static final ConcurrentMap<Pair<String, String>, AtomicInteger> appTopicCnt = new ConcurrentHashMap<>();

    /**
     * 记录内存里面延时消息数量的快照
     * key是当时的数量，value是记录的时间
     */
    private static final LinkedList<Pair<Integer, LocalDateTime>> memorySnapshot = new LinkedList<>();

    /**
     * increase message count under topic for appkey
     * @param appKey
     * @param topic
     */
    public static final void increaseTopicCnt(String appKey, String topic) {
        totalCnt.incrementAndGet();
        AtomicInteger topicCnt = appTopicCnt.computeIfAbsent(Pair.of(appKey, topic), (key) -> new AtomicInteger(0));
        topicCnt.incrementAndGet();
    }

    /**
     * 获取消息数量 并重置
     * @return
     */
    public static final int dumpTotalMessageCnt() {
        return totalCnt.getAndSet(0);
    }

    /**
     * 取出appkey+topic的数量
     * @return
     */
    public static final Map<Pair<String, String>, Integer> dumpTopicCnt() {
        Map<Pair<String, String>, Integer> data = new HashMap<>();
        if (appTopicCnt.isEmpty()) {
            return data;
        }

        appTopicCnt.forEach((k,v) -> {
            data.put(k, v.getAndSet(0));
        });

        return data;
    }

    /**
     * 计算当前内存里面的消息数量
     * @return
     */
    public static final int memorySnapshot() {
        int cnt = DelayMessageContainer.countMsg();
        if (cnt == 0) {
            return memorySnapshot.size();
        }
        memorySnapshot.add(Pair.of(cnt, LocalDateTime.now()));
        return memorySnapshot.size();
    }

    /**
     * 获取暂存的消息数量快照列表
     * @return
     */
    public static final List<Pair<Integer, LocalDateTime>> dumpMemorySnapshot() {
        List<Pair<Integer, LocalDateTime>> data = new ArrayList<>(memorySnapshot.size());
        while (memorySnapshot.peekFirst() != null) {
            data.add(memorySnapshot.pollFirst());
        }
        return data;
    }
}
