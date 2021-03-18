package com.illegalaccess.delay.cache;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Jimmy Li
 * 缓存模块统一配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "delay.cache")
public class DelayCacheProperties {

    /**
     * cache key version suffix
     */
    private String cacheVersion = "V1";
}
