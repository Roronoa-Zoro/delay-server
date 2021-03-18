package com.illegalaccess.delay.store.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * app topic 信息
 * @author Jimmy Li
 */
@Data
public class DelayMessageTopicDto implements Serializable {

    private Long id;
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
}
