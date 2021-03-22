package com.illegalaccess.delay.common.event;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * 延时消息事件
 * @author Jimmy Li
 */
@Setter
@Getter
@Builder
public class DelayMessageEvent implements Delayed {

    private ApplicationEvent applicationEvent;
    private Long delayMs;

    @Override
    public long getDelay(TimeUnit unit) {
        return delayMs - System.currentTimeMillis();
    }

    @Override
    public int compareTo(Delayed o) {

        long diff = getDelay(TimeUnit.MILLISECONDS) - o.getDelay(TimeUnit.MILLISECONDS);
        if (diff > 0) {
            return 1;
        } else if (diff < 0) {
            return -1;
        } else {
            return 0;
        }
    }
}
