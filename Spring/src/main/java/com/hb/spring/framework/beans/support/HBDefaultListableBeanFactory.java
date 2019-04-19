package com.hb.spring.framework.beans.support;

import com.hb.spring.framework.beans.config.HBBeanDefinition;
import com.hb.spring.framework.context.support.HBAbstractApplicationContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: Spring
 * @description: 加载配置的默认实现
 * @author: bobobo
 * @create: 2019-04-19 17:10
 **/
public class HBDefaultListableBeanFactory extends HBAbstractApplicationContext {

    //储存注册信息的BeanDefinition
    protected final Map<String, HBBeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String, HBBeanDefinition>();

}
