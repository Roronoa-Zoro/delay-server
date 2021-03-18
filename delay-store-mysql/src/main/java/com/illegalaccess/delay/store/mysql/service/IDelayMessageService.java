package com.illegalaccess.delay.store.mysql.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.illegalaccess.delay.store.enums.DelayMessageStatusEnum;
import com.illegalaccess.delay.store.entity.DelayMessage;

import java.util.Date;

/**
 * <p>
 * 延时消息表 服务类
 * </p>
 *
 * @author Jimmy Li
 * @since 2021-02-03
 */
public interface IDelayMessageService extends IService<DelayMessage> {

    boolean updateDelayMessage(String messageId, DelayMessageStatusEnum delayMessageStatus);

    int archiveDelayMessage(Date now);
}
