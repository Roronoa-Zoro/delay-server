package com.illegalaccess.delay.core.state;

import com.illegalaccess.delay.client.dto.DelayMessageReq;
import com.illegalaccess.delay.core.business.DelayServerBusiness;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * 服务运行状态
 *
 * @author Jimmy Li
 * @date 2021-02-01 17:38
 */
@Slf4j
@Component("delayServerRunningState")
public class DelayServerRunningState extends AbstractDelayServerState {

    @Autowired
    private DelayServerBusiness delayServerBusiness;

    @Override
    public String acceptMessage(DelayMessageReq req) {
        String messageId = delayServerBusiness.storeDelayMessage(req);
        log.info("message is saved");

        delayServerBusiness.scheduleInMemory(req, messageId);
        return messageId;
    }

    /**
     * 切换到热balance state， 完成正在处理的一条记录，然后执行热balance的逻辑
     * 会清空本地的数据，内部会加锁
     * 如果当前任务正在执行，那么情况数据集后，下一次取不到要执行的数据
     * 如果取任务和清数据集发生的并发操作，
     *      那么要么是先取到一条任务，然后执行，
     *      要么是先清空数据，然后在取数据，没有取到，发生阻塞
     *
     * 不管是哪种情况，都会只处理一条记录，当然这个处理过程可能会和rebalance的过程同时发生，
     * 如果处理的比较慢，rebalance执行的比较快，那么如果这条记录所属的slot又被分配给了其他机器，
     * 那么这个记录会被其他机器再次加载，然后再次投递到mq
     * 这个问题的影响和代价
     *   1。概率比较小
     *   2。本系统保证的至少投递一次
     *   3。本身消费者也需要保证消费逻辑的幂等性
     * 所以没有问题
     */
    @Override
    public void transform() {
        super.getDelayServerContext().setState(super.getDelayServerContext().rebalanceState);
        delayServerBusiness.pauseDispatch();
    }
}
