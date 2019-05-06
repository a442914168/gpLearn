package com.hb.spring.framework.aop;

/**
 * @program: Spring
 * @description:
 * @author: bobobo
 * @create: 2019-05-06 20:13
 **/
public interface HBAopProxy {
    
    Object getProxy();

    Object getProxy(ClassLoader classLoader);

}
