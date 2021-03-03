# delay-server
delay-message-system




### 模块说明
delay-bootstrap：启动模块    
delay-client：客户端SDK模块   
delay-client-dubbo：dubbo协议的客户端    
delay-client-http： http协议的客户端   
delay-core：核心逻辑    
delay-exporter: 接口协议暴露层，如http，dubbo    
delay-protocol：协议层，屏蔽具体使用的技术   
delay-protocol-zk：基于zk的协调中心    
delay-protocol-etcd：基于etcd的协调中心    
delay-store：存储层，屏蔽底层使用的技术   
delay-store-cassandra：使用cassandra作为数据存储   
delay-store-mysql：使用MySQL作为数据存储   

### 功能列表
1. 支持dubbo协议对外提供服务   
2. 支持http协议对外提供服务    
3. 底层存储支持mysql和Cassandra    
4. 协议协调层目前支持了etcd    


### UI展示
1 消息的总量   
2 按Appkey维度的消息总量   
3 接入的App的数量    
4 每个服务机器在内存中的消息的数量展示，定时快照存储到数据库       
5 取消消息的功能   
6 按App维度的，消息数量的时间趋势（比如每隔10分钟一个打点），按天展示，最大7天   
7 打通消息服务，可以在本系统直接申请新topic    


### 使用方式
1. clone代码   
2. 选择使用的接口协议和底层存储   
3. 安装etcd   
4. 安装kafka    
5. 安装mysql或者Cassandra   
6. 安装redis     
7. 在对应的配置文件里面配置存储的地址和kafka的地址    
```text
application-cassandra-dubbo.yml，使用Cassandra存储，暴露dubbo协议接口    
application-cassandra-http.yml，使用Cassandra存储，暴露http协议接口    
application-mysql-dubbo.yml，使用mysql存储，暴露dubbo协议接口    
application-mysql-dubbo.yml，使用mysql存储，暴露http协议接口    
application-custom.yml，自定义存储，协议或其他         
```
8. 配置appkey和对应的topic, 使用数据表delay_message_app, delay_message_topic    

### 配置说明
```yaml
delay: 
  core: 
    # unit is minute, when delay time within this value, will store into memory
    withinMin: 5
  common: 
    # suffix version for cache key
    cacheVersion: V1
  message: 
    # if carry delay server unique msg id
    carryMsgId: true
  protocol: # for etcd or zookeeper
    # server list, splitting with comma
    endpoints: 127.0.0.1:2379
    # username for server if needed
    user: user1
    # password for server if needed
    password: password
    # time to live for registered server node ,unit is second
    ttl: 9
    # delay time before triggering rebalance when server list or slot is changed, default is 1000ms, unit is ms
    rebalanceDelay: 1000
    # path to store etcd leaseId
    leasePath: /export/Logs/lease/
  store: 
    # whether to archive completed delay message, delete data when set to true
    archive: true
    # how often to trigger archive, below is default value
    archiveScheduler: 0 0 0/3 * * ?
```

### todo list
[ ] cassandra做存储层测试   
[ ] 管理后台开发   
[ ] 取消消息, 如果消息不是本机处理的，而且进入到内存后，需要自动路由到对应的机器，让该机器从内存里删掉该消息   
[ ] mysql存储层，清理数据的逻辑，目前没有根据slot作为条件，而是全部删除了，后续优化    

### 开发指南
#### 存储层开发
1. 可以自定义新项目，引入delay-store模块的依赖，实现对应的接口，相关的bean服务建议通过springboot listener的方式被扫描      
2. 在delay-bootstrap模块的配置文件里面，排除掉模块的存储模块依赖，添加自己的依赖   
3. 在application-customer.yml文件里面配置相关参数，构建包时勾选customer的profile，然后部署    

