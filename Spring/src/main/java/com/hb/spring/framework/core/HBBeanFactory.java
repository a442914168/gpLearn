package com.hb.spring.framework.core;

/**
 * @program: Spring
 * @description: 单例工厂的顶层设计
 * @author: bobobo
 * @create: 2019-04-19 17:00
 **/
public interface HBBeanFactory {

    /**
     * 根据beanName从IOC容器中获得一个实例Bean
     * @param beanName
     * @return
     * @throws Exception
     */
    Object getBean(String beanName) throws Exception;

    /**
     * 根据类获取bean
     * @param beanClass
     * @return
     * @throws Exception
     */
    public Object getBean(Class<?> beanClass) throws Exception;
}
