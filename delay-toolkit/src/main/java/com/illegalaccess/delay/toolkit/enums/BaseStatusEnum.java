package com.illegalaccess.delay.toolkit.enums;

public interface BaseStatusEnum {

    String getStatus();

    String getMessage();

    String toJSONString();

    default int code() {
        return Integer.valueOf(this.getStatus());
    }
}
