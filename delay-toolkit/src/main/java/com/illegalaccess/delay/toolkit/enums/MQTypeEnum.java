package com.illegalaccess.delay.toolkit.enums;

/**
 *
 * @author Jimmy Li
 * @date 2021-03-01 10:40
 */

public enum MQTypeEnum {
    Kafka(1, "kafka"),
    ActiveMQ(2, "active-mq"),
    RedisPubSub(3, "redis-pub-sub-mq");

    private Integer type;
    private String name;

    MQTypeEnum(Integer type, String name) {
        this.type = type;
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public static MQTypeEnum getByType(Integer type) {
        for (MQTypeEnum typeEnum : MQTypeEnum.values()) {
            if (typeEnum.getType().compareTo(type) == 0) {
                return typeEnum;
            }
        }

        return Kafka;
    }
}
