package com.illegalaccess.delay.store.dto;

import lombok.Data;

import java.io.Serializable;

/**
 *
 * @author Jimmy Li
 * @date 2021-03-04 10:29
 */
@Data
public class DelayMessageAppDto implements Serializable {

    private String appKey;

    /**
     * 接入方秘钥
     */
    private String appSecret;

    /**
     * mq的类型
     */
    private Integer mqType;

    /**
     * 额外信息
     */
    private String ext;

    private String appId;

    private String appDesc;

    private String creatorOrg;

    private String creator;
}
