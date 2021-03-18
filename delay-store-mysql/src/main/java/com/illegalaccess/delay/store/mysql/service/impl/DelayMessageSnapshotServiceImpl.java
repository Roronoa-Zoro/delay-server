package com.illegalaccess.delay.store.mysql.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.illegalaccess.delay.store.entity.DelayMessageSnapshot;
import com.illegalaccess.delay.store.mysql.mapper.DelayMessageSnapshotMapper;
import com.illegalaccess.delay.store.mysql.service.IDelayMessageSnapshotService;
import org.springframework.stereotype.Service;


/**
 * <p>
 * 延时消息内存消息数量快照 服务类
 * </p>
 *
 * @author Jimmy Li
 * @since 2021-02-03
 */
@Service
public class DelayMessageSnapshotServiceImpl extends ServiceImpl<DelayMessageSnapshotMapper, DelayMessageSnapshot> implements IDelayMessageSnapshotService {
}
