package com.illegalaccess.delay.protocol.etcd.auto;

import com.illegalaccess.delay.protocol.support.ProtocolProperties;
import com.illegalaccess.delay.protocol.etcd.EtcdResourceProtocol;
import com.illegalaccess.delay.protocol.event.ResourceChangeListener;
import com.illegalaccess.delay.toolkit.thread.DelayThreadFactory;
import io.etcd.jetcd.Client;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ScheduledExecutorFactoryBean;
import org.springframework.util.Assert;

/**
 * auto configuration for etcd module
 * @date 2021-03-04 10:28
 * @author Jimmy Li
 */
@Slf4j
@Configuration
@ConditionalOnClass(name = "io.etcd.jetcd.ClientBuilder")
@EnableConfigurationProperties({ProtocolProperties.class})
public class JEtcdAutoConfiguration {

    @Bean(destroyMethod = "close")
    public Client buildEtcdClient(ProtocolProperties protocolProperties) {
        Assert.notEmpty(protocolProperties.getEndpoints(), "protocol endpoint can not be empty");
//        Assert.notNull(protocolProperties.getUser(), "protocol username can not be null");
//        Assert.notNull(protocolProperties.getPassword(), "protocol password can not be null");
        Client client = Client.builder().endpoints(protocolProperties.getEndpoints().toArray(new String[0]))
                .authority(protocolProperties.getUser() + ":" + protocolProperties.getPassword())
                .build();
        log.info("jetcd client is build");
        return client;
    }

    @Bean
    public EtcdResourceProtocol etcdResourceProtocol() {
        log.info("etcd resource protocol is build");
        return new EtcdResourceProtocol();
    }

    @Bean(value = "leaseScheduledExecutorService", destroyMethod = "destroy")
    public ScheduledExecutorFactoryBean scheduledExecutorTask() {
        ScheduledExecutorFactoryBean scheduledExecutorFactoryBean = new ScheduledExecutorFactoryBean();
        scheduledExecutorFactoryBean.setPoolSize(1);
        scheduledExecutorFactoryBean.setThreadFactory(new DelayThreadFactory("etcd-lease-thread-"));
        return scheduledExecutorFactoryBean;
    }


}
