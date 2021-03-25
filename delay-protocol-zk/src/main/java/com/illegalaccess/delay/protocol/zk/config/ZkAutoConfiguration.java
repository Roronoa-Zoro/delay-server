package com.illegalaccess.delay.protocol.zk.config;

import com.google.common.base.Joiner;
import com.illegalaccess.delay.protocol.support.ProtocolProperties;
import com.illegalaccess.delay.protocol.zk.ZookeeperResourceProtocol;
import com.illegalaccess.delay.toolkit.Constants;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.ACLProvider;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.util.List;

@ConditionalOnClass(name = "org.apache.curator.framework.CuratorFrameworkFactory")
@Configuration
public class ZkAutoConfiguration {

    @Bean(name = "delayCuratorFramework", initMethod = "start")
    public CuratorFramework curatorFramework(ProtocolProperties protocolProperties) {

        String connectionUrl = Joiner.on(",").join(protocolProperties.getEndpoints());
        CuratorFrameworkFactory.Builder builder =  CuratorFrameworkFactory.builder().connectString(connectionUrl)
                .sessionTimeoutMs(protocolProperties.getTtl() * 1000)
                .connectionTimeoutMs(protocolProperties.getTimeout())
                .namespace("/delay-server");

        if (!StringUtils.isEmpty(protocolProperties.getUser()) && !StringUtils.isEmpty(protocolProperties.getPassword())) {
            String auth = Joiner.on(Constants.colon).join(protocolProperties.getUser(), protocolProperties.getPassword());
            builder.authorization("digest", auth.getBytes())
                    .aclProvider(new ACLProvider() {
                        @Override
                        public List<ACL> getDefaultAcl() {
                            return ZooDefs.Ids.CREATOR_ALL_ACL;
                        }

                        @Override
                        public List<ACL> getAclForPath(String path) {
                            return ZooDefs.Ids.CREATOR_ALL_ACL;
                        }
                    });
        }
        return builder.build();
    }

    @Bean
    public ZookeeperResourceProtocol zookeeperResourceProtocol() {
        return new ZookeeperResourceProtocol();
    }

}
