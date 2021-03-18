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
public class SaveAppKeyReq implements Serializable {

    private static final long serialVersionUID = -1L;

    /**
     * 接入方说明
     */
    private String appDesc;

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
}
