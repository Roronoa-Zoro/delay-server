package com.illegalaccess.delay.common.bean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.util.Map;

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

    public static <T> T getBean(Class<T> clz, String name) {
        Assert.notNull(beanFactory, "spring context can not be null");
        return beanFactory.getBean(name, clz);
    }

    public static <T> Map<String, T> getBeansOfType(@Nullable Class<T> clz) throws BeansException {
        return beanFactory.getBeansOfType(clz);
    }


}
