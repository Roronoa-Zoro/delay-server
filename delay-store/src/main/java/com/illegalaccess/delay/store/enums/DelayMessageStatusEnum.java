package com.illegalaccess.delay.store.enums;

import lombok.Getter;

/**
 * 消息状态枚举
 * @author Jimmy Li
 * @date 2021-03-04 10:29
 */

@Getter
public enum DelayMessageStatusEnum {

    InValid(0, "无效"),
    Valid(1, "有效"),
    Sent(2, "已发送"),
    ;

    private Integer status;
    private String desc;

    DelayMessageStatusEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }
}
