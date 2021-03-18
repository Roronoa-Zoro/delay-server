package com.illegalaccess.delay.toolkit.enums;

import com.illegalaccess.delay.toolkit.json.JsonTool;

public enum DelayStatusEnums implements BaseStatusEnum {

    SUCCESS("000000", "成功"),
    ;

    private String status;
    private String message;

    DelayStatusEnums(String status, String message) {
        this.status = status;
        this.message = message;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String toJSONString() {
        return JsonTool.toJsonString(this);
    }
}
