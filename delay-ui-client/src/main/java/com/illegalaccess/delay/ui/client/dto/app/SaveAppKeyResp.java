package com.illegalaccess.delay.ui.client.dto.app;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaveAppKeyResp implements Serializable {

    private static final long serialVersionUID = -1L;

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
}
