package com.illegalaccess.delay.core.delay;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * 延时消息对象
 * @author Jimmy Li
 * @date 2021-03-04 10:18
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DelayMessageObj implements Delayed {

    /**
     * future timestamp
     */
    private Long ttl;

    private String appKey;

    /**
     * mq topic
     */
    private String topic;

    /**
     * user sent message
     */
    private String message;

    /**
     * generated unique message id
     */
    private String messageId;

    private Long repeatInterval;

    @Override
    public long getDelay(TimeUnit unit) {
        return ttl - System.currentTimeMillis();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DelayMessageObj)) {
            return false;
        }
        DelayMessageObj that = (DelayMessageObj) o;
        return messageId.equals(that.messageId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(messageId);
    }
}
