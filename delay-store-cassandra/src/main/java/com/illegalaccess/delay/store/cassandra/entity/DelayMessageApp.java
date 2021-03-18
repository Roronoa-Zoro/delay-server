package com.illegalaccess.delay.store.cassandra.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author Jimmy Li
 * @since 2021-02-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Table("delay_message_app")
public class DelayMessageApp implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 接入方appid
     */
    @PrimaryKey
    private String appId;

    /**
     * 接入方appkey
     */
    @Column("app_key")
    private String appKey;

    /**
     * 接入方秘钥
     */
    @Column("app_secret")
    private String appSecret;

    /**
     * 1-有效
     */
    @Column("status")
    private Integer status;

    /**
     * 接入方说明
     */
    @Column("app_desc")
    private String appDesc;

    /**
     * 额外信息
     */
    @Column("ext")
    private String ext;

    @Column("create_time")
    private LocalDateTime createTime;

    @Column("update_time")
    private LocalDateTime updateTime;
}
