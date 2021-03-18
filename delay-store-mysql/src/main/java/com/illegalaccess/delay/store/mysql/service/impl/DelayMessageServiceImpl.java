package com.illegalaccess.delay.store.mysql.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.illegalaccess.delay.store.enums.DelayMessageStatusEnum;
import com.illegalaccess.delay.store.entity.DelayMessage;
import com.illegalaccess.delay.store.mysql.mapper.DelayMessageMapper;
import com.illegalaccess.delay.store.mysql.service.IDelayMessageService;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 延时消息表 服务实现类
 * </p>
 *
 * @author Jimmy Li
 * @since 2021-02-03
 */
@Service
public class DelayMessageServiceImpl extends ServiceImpl<DelayMessageMapper, DelayMessage> implements IDelayMessageService {

    @Override
    public boolean updateDelayMessage(String messageId, DelayMessageStatusEnum delayMessageStatus) {
        return baseMapper.updateDelayMessage(messageId, delayMessageStatus.getStatus());
    }

    @Override
    public int archiveDelayMessage(Date now) {
        QueryWrapper<DelayMessage> wrapper = new QueryWrapper<>();
        wrapper.in("status", DelayMessageStatusEnum.InValid.getStatus(), DelayMessageStatusEnum.Sent.getStatus())
                .le("exec_time", now);

        return baseMapper.delete(wrapper);
    }
}
