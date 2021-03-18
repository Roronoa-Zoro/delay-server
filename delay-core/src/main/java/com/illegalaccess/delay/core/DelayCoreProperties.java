package com.illegalaccess.delay.core;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * core模块的配置
 * @author Jimmy Li
 * @date 2021-03-04 10:20
 */
@Data
@Component
@ConfigurationProperties(prefix = "delay.core")
public class DelayCoreProperties {

    /**
     * 在多少 min 之内
     */
    private Integer withinMin = 5;

    /**
     * 轮训延时消息相关线程池大小
     */
    private int loopThreadSize = 3;

    /**
     * 处理延时消息发送和更新状态的线程池的大小
     */
    private int workingThreadSize = 4;

    /**
     * 如果需要考虑消息投递到broker，在投递到消费者的网络耗时，可以配置此参数
     * 即用实际执行时间 - networkDelay
     */
    private long networkDelay = 0L;

    /**
     * 内部topic循环的时间间隔，默认6分钟，单位秒
     */
    private int innerTopicRepeatInterval = 6 * 60;

}
