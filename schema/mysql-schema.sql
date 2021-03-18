-- create sql for MySQL

CREATE TABLE `delay_message` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'pk',
  `msg_id` varchar(50) NOT NULL DEFAULT '' COMMENT '消息id，唯一',
  `exec_time` bigint NOT NULL COMMENT '执行时间',
  `msg_content` varchar(200) NOT NULL DEFAULT '' COMMENT '消息内容',
  `topic` varchar(30) NOT NULL DEFAULT '' COMMENT 'mq topic',
  `status` int(11) NOT NULL DEFAULT 1 COMMENT '1=有效',
  `modify_time` datetime NOT NULL COMMENT '更新时间',
  `app_key` varchar(30) NOT NULL DEFAULT '',
  `slot` int(11) NOT NULL COMMENT '所属的槽',
  `repeat_type` int(11) not null default 0 comment '0=一次性消息，1=循环消息',
  `repeat_interval` int(11) not null default 0 comment '循环消息时下次消息的延时时间，即当前时间+该字段，单位ms',
  PRIMARY KEY (`id`),
  KEY `idx_msg_id` (`msg_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='延时消息表';

create index idx_msg_id on delay_message(msg_id);
create index idx_exec_time on delay_message(exec_time);

-- 插入内部使用的延时消息，每6分钟重复一次
insert into delay_message(msg_id, exec_time, msg_content, topic, status,modify_time,app_key,slot,repeat_type,repeat_interval) values
('eae3bdeafad443d381fbd11c61cbc38f.1', REPLACE(unix_timestamp(date_add(current_timestamp(3), interval 6 HOUR_MINUTE)),'.',''), '','delay_inner_topic',1,now(),'delay_server_inner_app_key',1,0, 360000);


CREATE TABLE `delay_message_app` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `app_id` varchar(50) NOT NULL DEFAULT '' COMMENT '接入方appid',
  `app_key` varchar(50) NOT NULL DEFAULT '' COMMENT '接入方appkey',
  `app_secret` varchar(50) NOT NULL DEFAULT '' COMMENT '接入方秘钥',
  `status` int(11) NOT NULL DEFAULT 1 COMMENT '1-有效',
  `app_desc` varchar(50) NOT NULL DEFAULT '' COMMENT '接入方说明',
  `mq_type` int(11) DEFAULT NULL COMMENT 'mq类型',
  `ext` varchar(128) DEFAULT NULL COMMENT '额外信息',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `creator` varchar(30) DEFAULT NULL COMMENT '创建者id',
  `creator_org` varchar(256) DEFAULT NULL COMMENT '创建者组织机构',
  PRIMARY KEY (`id`),
  KEY `idx_app_key` (`app_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='延时消息接入方表';

insert into delay_message_app(app_id, app_key, app_secret, status, app_desc, mq_type, ext, create_time, update_time, creator, creator_org) values
('8656600a7dfa465d9e83d1c64da8c097', 'delay_server_inner_app_key', 'b960611bb91a4784b4dd50b1aec8a1c4', 1, '延时服务App', 1, '', now(), now(), '你的id', '你的组织机构');


CREATE TABLE `delay_message_topic` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `app_key` varchar(50) NOT NULL DEFAULT '' COMMENT '接入方appkey',
  `mq_type` int(11) DEFAULT NULL COMMENT 'mq类型',
  `topic` varchar(128) DEFAULT NULL COMMENT 'ma的topic',
  `status` int(11) NOT NULL DEFAULT 1 COMMENT '1-有效',
  `creator` varchar(30) DEFAULT NULL COMMENT '创建者id',
  `creator_org` varchar(256) DEFAULT NULL COMMENT '创建者组织机构',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_app_key` (`app_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='延时消息接入方topic表';

insert into delay_message_topic(app_key, mq_type, topic, status, creator, creator_org, create_time, update_time) values
('delay_server_inner_app_key', 1, 'delay_inner_topic', 1, '你的id', '你的组织机构', now(), now());


CREATE TABLE `delay_message_stat` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT,
  `app_key` varchar(30) NOT NULL DEFAULT 'appkey',
  `message_received_time` datetime NOT NULL COMMENT '消息收到的时间，单位到小时',
  `message_received_cnt` int(11) NOT NULL COMMENT '消息收到的数量',
  `topic` varchar(120) DEFAULT NULL COMMENT 'topic',
  PRIMARY KEY (`id`),
  KEY `idx_msg_receive_time` (`message_received_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='延时消息统计表';

CREATE TABLE `delay_message_snapshot` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT,
  `host_ip` varchar(30) NOT NULL DEFAULT '机器IP',
  `message_cnt` int(11) NOT NULL COMMENT '消息收到的数量',
  `snapshot_time` datetime NOT NULL COMMENT '生成快照的时间',

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='延时消息内存消息数量快照表';


