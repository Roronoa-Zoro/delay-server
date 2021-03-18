package com.illegalaccess.delay.store.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * app所属的消息数量
 * @author Jimmy Li
 * @date 2021-03-04 10:29
 */
@Data
public class AppKeyMessageCntDto implements Serializable {

    private String appKey;

    private Long count;
}
