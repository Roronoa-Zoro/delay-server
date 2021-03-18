-- create sql for cassandra
create keyspace delay_server with replication = {'class': 'SimpleStrategy', 'replication_factor' : 1};

alter keyspace delay_server with replication = {'class': 'SimpleStrategy', 'replication_factor' : 1};



use delay_server;



select * from delay_message;

create table delay_message(
   -- id bigint,
   msg_id varchar,
   exec_time bigint,
   msg_content varchar,
   topic varchar,
   status int,
   app_key varchar,
   slot int,
   modify_time timestamp,
   repeat_type int,
   repeat_interval int,
   primary key ( msg_id, slot )
);

-- 内部使用的延时消息
insert into delay_message(msg_id, exec_time, msg_content, topic, status,modify_time,app_key,slot,repeat_type,repeat_interval) values
('eae3bdeafad443d381fbd11c61cbc38f.1', tounixtimestamp(now()), '','delay_inner_topic',1,dateof(now()),'delay_server_inner_app_key',1,0, 360000);


-- create index idx_msg_id on delay_message(msg_id);

create table delay_message_app(
   app_id varchar primary key,
   app_key varchar,
   app_secret varchar,
   app_desc varchar,
   status int,
   ext varchar,
   create_time timestamp,
   update_time timestamp,
   creator varchar,
   creator_org varchar,
);

create index idx_app_key on delay_message_app(app_key);

-- 内部使用的延时消息的App
insert into delay_message_app(app_id, app_key, app_secret, status, app_desc, ext, create_time, update_time, creator, creator_org) values
('8656600a7dfa465d9e83d1c64da8c097', 'delay_server_inner_app_key', 'b960611bb91a4784b4dd50b1aec8a1c4', '延时服务App', 1, '', now(), now(), '你的id', '你的组织机构');


create table delay_message_stat(
   app_key varchar primary key,
   topic varchar primary key,
   message_received_cnt int,
   message_received_time timestamp,
   primary key ( app_key, topic, message_received_time)
);

-- create index idx_stat_app_key on delay_message_stat(app_key);

CREATE TABLE delay_message_topic (
    id int,
    app_key varchar,
    mq_type int,
    topic varchar,
    status int,
    creator varchar,
    creator_org varchar,
    create_time timestamp,
    update_time timestamp,
    primary key ( app_key, topic,id )
) ;

insert into delay_message_topic(id,app_key, mq_type, topic, status, creator, creator_org, create_time, update_time) values
(1, 'delay_server_inner_app_key', 1, 'delay_server_inner_topic', 1, '你的id', '你的组织机构', now(), now());


CREATE TABLE delay_message_snapshot (
    id bigint,
    host_ip varchar,
    snapshot_time timestamp,
    message_cnt int,
    PRIMARY KEY (host_ip, snapshot_time)
);

