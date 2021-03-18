package com.illegalaccess.delay.common.bean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigPostProcessor implements BeanFactoryPostProcessor {

    /**
     * init bean factory before using
     * @param beanFactory
     * @throws BeansException
     */
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        DelayBeanFactory bf = new DelayBeanFactory();
        bf.setBeanFactory(beanFactory);
    }
}
