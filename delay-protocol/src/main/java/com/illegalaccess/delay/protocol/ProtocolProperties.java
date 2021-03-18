package com.illegalaccess.delay.protocol;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 * 资源协议配置属性
 * @author Jimmy Li
 * @date 2021-01-27 20:43
 */
@Data
@Component
@ConfigurationProperties(prefix = "delay.protocol")
public class ProtocolProperties {

    /**
     * 服务连接端点
     */
    private List<String> endpoints;

    /**
     * 安全认证的用户名
     */
    private String user;

    /**
     * 安全认证的用密码
     */
    private String password;

    /**
     * 节点存活时间，单位秒
     */
    private Integer ttl = 9;


    private Integer timeout;

    /**
     * 延迟多久进行rebalance
     * 单位毫秒
     */
    private Long rebalanceDelay = 1000L;

    /**
     * leaseId 存储的位置
     */
    private String leasePath = "/export/Logs/lease/";
}
