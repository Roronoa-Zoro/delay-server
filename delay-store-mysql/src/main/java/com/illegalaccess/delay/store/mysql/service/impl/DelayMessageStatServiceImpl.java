package com.illegalaccess.delay.store.mysql.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.illegalaccess.delay.store.entity.DelayMessageStat;
import com.illegalaccess.delay.store.mysql.mapper.DelayMessageStatMapper;
import com.illegalaccess.delay.store.mysql.service.IDelayMessageStatService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 延时消息统计表 服务实现类
 * </p>
 *
 * @author Jimmy Li
 * @since 2021-02-03
 */
@Service
public class DelayMessageStatServiceImpl extends ServiceImpl<DelayMessageStatMapper, DelayMessageStat> implements IDelayMessageStatService {

}
