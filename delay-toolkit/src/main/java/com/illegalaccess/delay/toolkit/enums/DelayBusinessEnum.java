package com.illegalaccess.delay.toolkit.enums;

import com.illegalaccess.delay.toolkit.json.JsonTool;

public enum DelayBusinessEnum implements BaseStatusEnum {

    Cancel_Msg_Fail("100001", "取消延时消息失败"),
    ;

    private String status;
    private String message;

    DelayBusinessEnum(String status, String message) {
        this.status = status;
        this.message = message;
    }

    @Override
    public String getStatus() {
        return null;
    }

    @Override
    public String getMessage() {
        return null;
    }

    @Override
    public String toJSONString() {
        return JsonTool.toJsonString(this);
    }
}
