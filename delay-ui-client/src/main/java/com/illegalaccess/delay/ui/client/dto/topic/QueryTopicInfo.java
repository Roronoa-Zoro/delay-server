package com.illegalaccess.delay.ui.client.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class QueryTopicInfo implements Serializable {

    private static final long serialVersionUID = -1L;

    /**
     * 接入方appkey
     */
    private String appKey;

    /**
     * 1-有效
     */
    private Integer status;

    /**
     * mq的类型
     */
    private Integer mqType;

    /**
     * 额外信息
     */
    private String topic;

    /**
     * 创建者id
     */
    private String creator;

    /**
     * 创建者所在组织机构
     */
    private String creatorOrg;

    private LocalDateTime createTime;
}
