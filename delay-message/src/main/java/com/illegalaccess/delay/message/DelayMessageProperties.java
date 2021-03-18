package com.illegalaccess.delay.message;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 消息模块的配置属性
 * @author Jimmy Li
 * @date 2021-03-04 10:23
 */
@Data
@Component
@ConfigurationProperties(prefix = "delay.message")
public class DelayMessageProperties {
    /**
     * 消息服务的地址
     */
    private String address;

    /**
     * 消息服务的user
     */
    private String user;

    /**
     * 消息服务的password
     */
    private String password;

    private String topic;

    /**
     * 分组
     */
    private int group;

    /**
     * 是否携带本系统的唯一消息ID
     */
    private boolean carryMsgId = true;
}
