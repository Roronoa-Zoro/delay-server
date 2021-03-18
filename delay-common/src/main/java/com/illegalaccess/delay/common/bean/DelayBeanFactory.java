package com.illegalaccess.delay.common.bean;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.util.Assert;

public class DelayBeanFactory {

    private static ConfigurableListableBeanFactory beanFactory;

    void setBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        DelayBeanFactory.beanFactory = beanFactory;
    }

    public static <T> T getBean(String name) {
        Assert.notNull(beanFactory, "spring context can not be null");
        return (T) beanFactory.getBean(name);
    }

    public static <T> T getBean(Class<T> clz) {
        Assert.notNull(beanFactory, "spring context can not be null");
        return beanFactory.getBean(clz);
    }

}
