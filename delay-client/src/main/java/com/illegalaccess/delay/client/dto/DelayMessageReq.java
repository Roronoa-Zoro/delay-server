package com.illegalaccess.delay.client.dto;

import java.io.Serializable;

/**
 * {
 * appKey:"给业务颁发的key",
 * topic:"消息需要发到的topic",
 * delay:"要延时多久",
 * delayUnit:"延时的时间单位,天，时，分，秒",
 * message:"json格式的原始数据",
 * messageId:"每次请求的消息id，全局唯一，业务不指定则由框架设置"
 * }
 * <p>
 * 延时消息请求
 *
 * @author Jimmy Li
 * @date 2021-01-27 20:23
 */

public class DelayMessageReq implements Serializable {

    private static final long serialVersionUID = -5475377465552623658L;
    private String appKey;

    private String topic;

    private Integer repeatInterval = 0;

    private String message;

    /**
     * 执行时间戳
     */
    private long execTime;

    public DelayMessageReq() {
    }

    public DelayMessageReq(String appKey, String topic, Integer repeatInterval, String message, long execTime) {
        this.appKey = appKey;
        this.topic = topic;
        this.repeatInterval = repeatInterval;
        this.message = message;
        this.execTime = execTime;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getExecTime() {
        return execTime;
    }

    public void setExecTime(long execTime) {
        this.execTime = execTime;
    }

    public Integer getRepeatInterval() {
        return repeatInterval;
    }

    public void setRepeatInterval(Integer repeatInterval) {
        this.repeatInterval = repeatInterval;
    }

    @Override
    public String toString() {
        return "DelayMessageReq{" +
                "appKey='" + appKey + '\'' +
                ", topic='" + topic + '\'' +
                ", repeatInterval=" + repeatInterval +
                ", message='" + message + '\'' +
                ", execTime=" + execTime +
                '}';
    }
}
