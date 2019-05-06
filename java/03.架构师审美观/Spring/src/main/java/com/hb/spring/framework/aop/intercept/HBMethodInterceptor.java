package com.hb.spring.framework.aop.intercept;

/**
 * @program: Spring
 * @description:
 * @author: bobobo
 * @create: 2019-05-06 20:59
 **/
public interface HBMethodInterceptor {

    Object invoke(HBMethodInvocation invocation) throws Throwable;
}
