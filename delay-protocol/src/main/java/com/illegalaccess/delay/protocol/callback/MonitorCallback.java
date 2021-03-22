package com.illegalaccess.delay.protocol.callback;

import com.illegalaccess.delay.protocol.enums.ResourceChangeTypeEnum;

public interface MonitorCallback {

    void callback(ResourceChangeTypeEnum resourceChangeType);
}
