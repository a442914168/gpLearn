package com.hb.spring.framework.aop.aspect;

import com.hb.spring.framework.aop.intercept.HBMethodInterceptor;
import com.hb.spring.framework.aop.intercept.HBMethodInvocation;

import java.lang.reflect.Method;

/**
 * @program: Spring
 * @description:
 * @author: bobobo
 * @create: 2019-05-06 20:49
 **/
public class HBAfterThrowingAdviceInterceptor extends HBAbstractAspectAdvice implements HBAdvice, HBMethodInterceptor {


    private String throwingName;

    public HBAfterThrowingAdviceInterceptor(Method aspectMethod, Object aspectTarget) {
        super(aspectMethod, aspectTarget);
    }

    public Object invoke(HBMethodInvocation mi) throws Throwable {
        try {
            return mi.proceed();
        } catch (Throwable e) {
            invokeAdviceMethod(mi, null, e.getCause());
            throw e;
        }
    }

    public void setThrowName(String throwName) {
        this.throwingName = throwName;
    }
}
