package com.illegalaccess.delay.store.mysql.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.illegalaccess.delay.store.entity.DelayMessageApp;
import com.illegalaccess.delay.store.mysql.mapper.DelayMessageAppMapper;
import com.illegalaccess.delay.store.mysql.service.IDelayMessageAppService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Jimmy Li
 * @since 2021-02-03
 */
@Service
public class DelayMessageAppServiceImpl extends ServiceImpl<DelayMessageAppMapper, DelayMessageApp> implements IDelayMessageAppService {

}
