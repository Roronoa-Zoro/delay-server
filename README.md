# delay-server
delay-message-system


### 概要设计思路    
依赖etcd作为协调者，分配一定数量的槽（slot），然后服务器执行重平衡过程，保证每台服务器被分配到一定比例的槽   
服务器接收延时消息时，把槽和信息一并写入底层存储，保证每台服务器处理的消息不会争抢，到期的消息会被投递到kafka      
当服务器数量或者槽的数量变更时，触发重平衡操作，重平衡结束后，继续执行上述第二步骤    


### 名词解释    
1. 槽(slot), 和kafka里面的partition概念类似    
2. 重平衡，和kafka里面的重平衡类似    


### 模块说明
delay-bootstrap：启动模块    
delay-core：核心逻辑    
delay-exporter: 接口协议暴露层，如http，dubbo    
delay-protocol：协议层，屏蔽具体使用的技术   
delay-protocol-zk：基于zk的协调中心    
delay-protocol-etcd：基于etcd的协调中心    
delay-store：存储层api，屏蔽底层使用的技术   
delay-store-cassandra：使用cassandra作为数据存储   
delay-store-mysql：使用MySQL作为数据存储   
delay-client：客户端SDK模块   
delay-client-dubbo：dubbo协议的客户端    
delay-client-http： http协议的客户端    
delay-message：下游消息系统的接口层   
delay-message-kafka：kafka作为下游消息系统的实现          


### 功能列表
1. 支持dubbo协议对外提供服务   
2. 支持http协议对外提供服务    
3. 底层存储支持mysql和Cassandra    
4. 协议协调层目前支持了etcd    
5. 支持任意时间点的延时消息   


### UI展示
1 消息的总量   
2 按Appkey维度的消息总量   
3 接入的App的数量    
4 每个服务机器在内存中的消息的数量展示，定时快照存储到数据库,趋势图展示           
5 取消消息的功能   
6 按App维度的，消息数量的时间趋势（比如每隔10分钟一个打点），按天展示，最大7天   
7 打通消息服务，可以在本系统直接申请新topic    


### 使用方式
1. clone代码   
2. 选择使用的接口协议和底层存储   
3. 安装etcd(推荐使用etcd做注册中心和协调层)      
3.1 分配槽(slot), 往路径/delay/slot/all-slot/写入，如分配4个槽，则格式为1,2,3,4     
3.2 zk的namespace是delay-server, 写入数据的时候注意这个    
4. 安装kafka, 创建内部使用的topic：delay_inner_topic    
5. 安装mysql或者Cassandra   
6. 安装redis     
7. 在对应的配置文件里面配置存储中间件的地址和kafka的地址    
8. 在管理后台分配appkey和topic     
```text
application-cassandra-dubbo.yml，使用Cassandra存储，暴露dubbo协议接口    
application-cassandra-http.yml，使用Cassandra存储，暴露http协议接口    
application-mysql-dubbo.yml，使用mysql存储，暴露dubbo协议接口    
application-mysql-dubbo.yml，使用mysql存储，暴露http协议接口    
application-custom.yml，自定义存储，协议或其他         
```   
9. 客户端接入   
9.1 如果使用dubbo协议，调用方引用delay-client模块，配置成dubbo consumer即可   
9.2 如果使用http协议，调用方可以引用delay-client模块，然后按照业务系统使用的rest工具进行调用     

### 配置说明
```yaml
delay: 
  core: 
    # unit is minute, when delay time within this value, will store into memory
    withinMin: 5
  common: 
    # suffix version for cache key
    # 缓存版本号后缀    
    cacheVersion: V1
  message: 
    # if carry delay server unique msg id
    # 是否把本系统的唯一id进行下发携带到mq   
    carryMsgId: true
  protocol: # for etcd or zookeeper
    # server list, splitting with comma
    endpoints: 127.0.0.1:2379
    # username for server if needed
    user: user1
    # password for server if needed
    password: password
    # time to live for registered server node ,unit is second
    # 注册节点的有效时间,单位秒    
    ttl: 9
    # delay time before triggering rebalance when server list or slot is changed, default is 1000ms, unit is ms
    # 触发重平衡之前的延时，单位毫秒    
    rebalanceDelay: 1000
    # path to store etcd leaseId
    # etcd 的leaseid写入本地盘的路径    
    leasePath: /export/Logs/lease/
  store: 
    # whether to archive completed delay message, delete data when set to true
    # 是否开启归档历史已完成消息   
    archive: true
    # how often to trigger archive, below is default value
    # 开启归档后，执行归档任务的时间表达式     
    archiveScheduler: 0 0 0/3 * * ?
```

### todo list
[ ] cassandra做存储层测试   
[ ] 管理后台开发   
[ ] 取消消息, 如果消息不是本机处理的，而且进入到内存后，需要自动路由到对应的机器，让该机器从内存里删掉该消息   
[ ] mysql存储层，清理数据的逻辑，目前没有根据slot作为条件，而是全部删除了，后续优化   
[ ] 消息多次重试后报警    

### 开发指南
#### 存储层开发
1. 可以自定义新项目，引入delay-store模块的依赖，实现对应的接口，相关的bean服务建议通过springboot listener的方式被扫描      
2. 在delay-bootstrap模块的配置文件里面，排除掉模块的存储模块依赖，添加自己的依赖   
3. 在application-customer.yml文件里面配置相关参数，构建包时勾选customer的profile，然后部署    

#### 消息层开发   
1. 自定义新项目或者模块，引入delay-message模块的依赖   
2. 实现DelayMqApi接口    
3. 实现对应的consumer，监听内部队列delay_inner_topic，实现逻辑参考DelayMessageKafkaListener    
4. 通过springboot listener的方式暴露配置依赖    
5. 在delay-bootstrap的custom profile里面依赖心的jar包        


