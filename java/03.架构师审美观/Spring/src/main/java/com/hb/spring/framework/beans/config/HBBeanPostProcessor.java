package com.hb.spring.framework.beans.config;

/**
 * @program: Spring
 * @description: 类的处理器，添加Before After的方法，用于AOP
 * @author: bobobo
 * @create: 2019-04-20 11:13
 **/
public class HBBeanPostProcessor {

    public Object postProcessBeforeInitialization(Object bean, String beanName) throws Exception {
        return bean;
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) throws Exception {
        return bean;
    }



}
