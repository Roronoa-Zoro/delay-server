package com.illegalaccess.delay.store;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 存储模块的配置属性
 * @author Jimmy Li
 * @date 2021-01-27 20:43
 */
@Data
@Component
@ConfigurationProperties(prefix = "delay.store")
public class DelayStoreProperties {

    /**
     * 是否归档已经处理的数据, 默认是
     * 目前支持的归档方式是直接删除已处理的数据
     */
    private boolean archive = true;

    /**
     * cron for archiving message
     * default, every 3 hours
     */
    private String archiveScheduler = "0 0 0/3 * * ?";
}
