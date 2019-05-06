package com.hb.spring.framework.aop.aspect;

import com.hb.spring.framework.aop.intercept.HBMethodInterceptor;
import com.hb.spring.framework.aop.intercept.HBMethodInvocation;

import java.lang.reflect.Method;

/**
 * @program: Spring
 * @description:
 * @author: bobobo
 * @create: 2019-05-06 20:47
 **/
public class HBAfterReturningAdviceInterceptor extends HBAbstractAspectAdvice implements HBAdvice, HBMethodInterceptor {

    private HBJoinPoint joinPoint;

    public HBAfterReturningAdviceInterceptor(Method aspectMethod, Object aspectTarget) {
        super(aspectMethod, aspectTarget);
    }

    public Object invoke(HBMethodInvocation invocation) throws Throwable {
        Object retVal = invocation.proceed();
        this.joinPoint = invocation;
        this.afterReturning(retVal, invocation.getMethod(), invocation.getArguments(), invocation.getThis());
        return retVal;
    }

    private void afterReturning(Object retVal, Method method, Object[] arguments, Object aThis) throws Throwable {
        super.invokeAdviceMethod(this.joinPoint, retVal, null);
    }
}
