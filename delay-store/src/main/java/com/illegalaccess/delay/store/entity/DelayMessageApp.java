package com.illegalaccess.delay.store.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 一个人一个appkey，一个appkey下面绑定多个topic
 * </p>
 *
 * @author Jimmy Li
 * @since 2021-02-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DelayMessageApp implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 接入方appid
     */
    private String appId;

    /**
     * 接入方appkey
     */
    private String appKey;

    /**
     * 接入方秘钥
     */
    private String appSecret;

    /**
     * 1-有效
     */
    private Integer status;

    /**
     * 接入方说明
     */
    private String appDesc;

    /**
     * mq的类型
     */
    private Integer mqType;

    /**
     * 额外信息
     */
    private String ext;

    /**
     * 创建者id
     */
    private String creator;

    /**
     * 创建者所在组织机构
     */
    private String creatorOrg;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
