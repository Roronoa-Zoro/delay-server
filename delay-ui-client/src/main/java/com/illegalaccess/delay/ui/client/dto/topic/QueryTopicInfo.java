package com.illegalaccess.delay.ui.client.dto.topic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QueryTopicInfo implements Serializable {

    private static final long serialVersionUID = -1L;

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

//    private LocalDateTime createTime;
}
