package com.hb.spring.framework.aop;

import com.hb.spring.framework.aop.support.HBAdvisedSupport;

/**
 * @program: Spring
 * @description:
 * @author: bobobo
 * @create: 2019-05-06 20:15
 **/
public class HBCglibAopProxy implements HBAopProxy {

    public HBCglibAopProxy(HBAdvisedSupport config) {

    }

    public Object getProxy() {
        return null;
    }

    public Object getProxy(ClassLoader classLoader) {
        return null;
    }
}
