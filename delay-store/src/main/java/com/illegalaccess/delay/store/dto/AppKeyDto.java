package com.illegalaccess.delay.store.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class AppKeyDto implements Serializable {

    private String appId;

    private String appKey;

    private String appSecret;

    private String appDesc;

    private String creatorOrg;

    private String creator;


}
