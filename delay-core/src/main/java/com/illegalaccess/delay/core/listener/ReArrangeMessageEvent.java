package com.illegalaccess.delay.core.listener;

import com.illegalaccess.delay.core.delay.DelayMessageObj;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ReArrangeMessageEvent extends ApplicationEvent {

    private DelayMessageObj delayMessageObj;

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public ReArrangeMessageEvent(Object source) {
        super(source);
    }

    public ReArrangeMessageEvent(Object source, DelayMessageObj delayMessageObj) {
        super(source);
        this.delayMessageObj = delayMessageObj;
    }
}
