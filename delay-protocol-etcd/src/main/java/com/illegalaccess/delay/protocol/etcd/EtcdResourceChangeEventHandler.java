package com.illegalaccess.delay.protocol.etcd;

import com.illegalaccess.delay.protocol.ResourceProtocol;
import com.illegalaccess.delay.protocol.event.ResourceChangeEventHandler;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 资源变更处理器类
 * @author Jimmy Li
 */
public abstract class EtcdResourceChangeEventHandler extends ResourceChangeEventHandler {

    @Autowired
    private ResourceProtocol resourceProtocol;

    /**
     * logic for resource change event
     */
    @Override
    protected void handle() {
        resourceProtocol.rebalance();
    }
}
