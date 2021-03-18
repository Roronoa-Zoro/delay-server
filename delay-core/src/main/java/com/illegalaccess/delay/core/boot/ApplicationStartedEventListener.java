package com.illegalaccess.delay.core.boot;

import com.illegalaccess.delay.common.event.DelayEventPublisher;
import com.illegalaccess.delay.core.DelayCoreProperties;
import com.illegalaccess.delay.core.scheduler.DumpDelayMessageStatThreadPoolTask;
import com.illegalaccess.delay.core.scheduler.LoadDelayMessageThreadPoolTask;
import com.illegalaccess.delay.core.scheduler.LoopDelayMessageThreadPoolTask;
import com.illegalaccess.delay.message.DelayMqApi;
import com.illegalaccess.delay.protocol.ResourceProtocol;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * server start up logic
 * 服务启动时相关注册操作
 * @date 2021-03-04 10:17
 * @author Jimmy Li
 */
@Slf4j
@Component
public class ApplicationStartedEventListener implements ApplicationListener<ApplicationStartedEvent>, Ordered {

    @Autowired
    private ResourceProtocol resourceProtocol;

    @Resource(name = "loopDelayQueueExecutor")
    private ScheduledThreadPoolExecutor loopThread;

    @Resource(name = "processDelayQueueExecutor")
    private ThreadPoolExecutor workingThread;

    @Autowired
    private DelayMqApi delayMqApi;

    @Autowired
    private DelayCoreProperties delayCoreProperties;

    @Autowired
    private DelayEventPublisher delayEventPublisher;

    @Override
    public void onApplicationEvent(ApplicationStartedEvent applicationStartedEvent) {
        resourceProtocol.monitorRegisterStatus();
        log.info("protocol is monitored");

        resourceProtocol.register();
        log.info("protocol is registered");

        loopThread.scheduleAtFixedRate(new LoopDelayMessageThreadPoolTask(workingThread, delayMqApi, delayEventPublisher), 3, 1, TimeUnit.SECONDS);
        log.info("scheduled loop delay message task");

        loopThread.scheduleAtFixedRate(new LoadDelayMessageThreadPoolTask(delayCoreProperties), 0, 1, TimeUnit.MINUTES);
        log.info("scheduled load delay message task");

        loopThread.scheduleAtFixedRate(new DumpDelayMessageStatThreadPoolTask(), 0, 2, TimeUnit.MINUTES);
        log.info("scheduled dump delay message task");
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
