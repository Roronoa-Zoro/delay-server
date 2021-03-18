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
        return 0;
    }

    @Override
    public int compareTo(Delayed o) {
        return 0;
    }
}
