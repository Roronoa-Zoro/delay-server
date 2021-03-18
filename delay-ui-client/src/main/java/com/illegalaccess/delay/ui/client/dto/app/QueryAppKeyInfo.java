package com.illegalaccess.delay.ui.client.dto.app;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QueryAppKeyInfo implements Serializable {

    private static final long serialVersionUID = -1L;

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
