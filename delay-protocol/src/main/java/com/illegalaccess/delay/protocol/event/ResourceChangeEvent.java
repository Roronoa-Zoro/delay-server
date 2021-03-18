package com.illegalaccess.delay.protocol.event;

import com.illegalaccess.delay.protocol.enums.ResourceChangeTypeEnum;
import org.springframework.context.ApplicationEvent;

/**
 * Resource change event
 * slot or server list change can cause this event
 * 资源变更事件
 * @date 2021-03-04 10:26
 * @author Jimmy Li
 */
public class ResourceChangeEvent extends ApplicationEvent {

    private ResourceChangeTypeEnum resourceChangeType;

    public ResourceChangeEvent(Object source, ResourceChangeTypeEnum resourceChangeType) {
        super(source);
        this.resourceChangeType = resourceChangeType;
    }

    @Override
    public String toString() {
        return "ResourceChangeEvent{" +
                "resourceChangeType=" + resourceChangeType +
                '}';
    }
}
