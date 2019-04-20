package com.hb.spring.framework.context.support;

/**
 * @program: Spring
 * @description: IOC容器实现的顶层设计
 * @author: bobobo
 * @create: 2019-04-19 17:04
 **/
public abstract class HBAbstractApplicationContext {

    /**
     * 受保护的，只提供给子类重写
     * 子类实现做 定位、加载、注册的操作
     * @throws Exception
     */
    protected void refresh() throws Exception {}
}
