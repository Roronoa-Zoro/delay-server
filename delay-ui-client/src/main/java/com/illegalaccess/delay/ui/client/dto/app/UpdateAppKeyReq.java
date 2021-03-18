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
public class UpdateAppKeyReq implements Serializable {

    private static final long serialVersionUID = -1L;

    private String appId;

    /**
     * 1-有效
     */
    private Integer status;

    /**
     * 接入方说明
     */
    private String appDesc;

    /**
     * 额外信息
     */
    private String ext;
}
