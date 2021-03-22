package com.illegalaccess.delay.protocol.callback;

import com.illegalaccess.delay.common.bean.DelayBeanFactory;
import com.illegalaccess.delay.common.event.DelayEventPublisher;
import com.illegalaccess.delay.protocol.support.ProtocolProperties;
import com.illegalaccess.delay.protocol.enums.ResourceChangeTypeEnum;
import com.illegalaccess.delay.protocol.event.ResourceChangeEvent;
import com.illegalaccess.delay.toolkit.TimeUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class ResourceChangeCallback implements MonitorCallback {

    @Override
    public void callback(ResourceChangeTypeEnum resourceChangeType) {
        DelayEventPublisher delayEventPublisher = DelayBeanFactory.getBean(DelayEventPublisher.class);
        ProtocolProperties protocolProperties = DelayBeanFactory.getBean(ProtocolProperties.class, "protocolProperties");

        log.info("resource:{} is changed", resourceChangeType);
        delayEventPublisher.publishEventWithDelay(new ResourceChangeEvent(resourceChangeType, resourceChangeType), TimeUtils.addTimeStamp(protocolProperties.getRebalanceDelay(), TimeUnit.MILLISECONDS));
    }
}
