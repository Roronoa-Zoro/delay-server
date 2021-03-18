package com.illegalaccess.delay.core.listener;

import com.illegalaccess.delay.core.delay.DelayMessageObj;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 *
 * @date 2021-02-08 19:45
 * @author Jimmy Li
 */
@Slf4j
@Component
public class ReArrangeMessageListener implements ApplicationListener<ReArrangeMessageEvent> {

    @Override
    public void onApplicationEvent(ReArrangeMessageEvent event) {
        DelayMessageObj obj = event.getDelayMessageObj();

        // 消息的执行时间和当前时间差，超过10分钟，则报警
    }
}
