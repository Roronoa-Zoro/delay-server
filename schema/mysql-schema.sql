-- create sql for MySQL

CREATE TABLE `delay_message` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'pk',
  `msg_id` varchar(50) NOT NULL DEFAULT '' COMMENT '消息id，唯一',
  `exec_time` datetime NOT NULL COMMENT '执行时间',
  `msg_content` varchar(200) NOT NULL DEFAULT '' COMMENT '消息内容',
  `topic` varchar(30) NOT NULL DEFAULT '' COMMENT 'mq topic',
  `status` int(11) NOT NULL DEFAULT 1 COMMENT '1=有效',
  `modify_time` datetime NOT NULL COMMENT '更新时间',
  `app_key` varchar(30) NOT NULL DEFAULT '',
  `slot` int(11) NOT NULL COMMENT '所属的槽',
  PRIMARY KEY (`id`),
  KEY `idx_msg_id` (`msg_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='延时消息表';

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
  `creatorOrg` varchar(256) DEFAULT NULL COMMENT '创建者组织机构',
  `creator_org` varchar(256) DEFAULT NULL COMMENT '创建者组织机构',
  PRIMARY KEY (`id`),
  KEY `idx_app_key` (`app_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='延时消息接入方表';

CREATE TABLE `delay_message_stat` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT,
  `app_key` varchar(30) NOT NULL DEFAULT '',
  `message_received_time` datetime NOT NULL COMMENT '消息收到的时间，单位到小时',
  `message_received_cnt` int(11) NOT NULL COMMENT '消息收到的数量',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='延时消息统计表';

CREATE TABLE `delay_message_app_auth` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'pk',
  `app_key` varchar(50) NOT NULL DEFAULT '' COMMENT '接入方appkey',
  `grantor` varchar(50) NOT NULL DEFAULT '' COMMENT '授权人',
  `status` int(11) NOT NULL DEFAULT 1 COMMENT '1-有效',
  `grantee` varchar(50) NOT NULL DEFAULT '' COMMENT '被授权人',
  `grantee_org` varchar(128) DEFAULT NULL COMMENT '被授权人组织机构',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_app_key` (`app_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='appKey的创建者，可以把该key授权给其他人，一般是相同团队的人，那么该人登录管理后台后，可以看到和创建者相同的数据视图';

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='延时消息接入方表';