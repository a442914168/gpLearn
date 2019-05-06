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
public class HBMethodBeforeAdviceInterceptor extends HBAbstractAspectAdvice implements HBAdvice, HBMethodInterceptor {

    private HBJoinPoint joinPoint;

    public HBMethodBeforeAdviceInterceptor(Method aspectMethod, Object aspectTarget) {
        super(aspectMethod, aspectTarget);
    }

    private void before(Method method, Object[] args, Object target) throws Throwable {
        //传入织入参数
        //method.invoke(target);
        super.invokeAdviceMethod(this.joinPoint,null,null);
    }

    public Object invoke(HBMethodInvocation invocation) throws Throwable {
        //从被织入的代码中才能拿到，JoinPoint
        this.joinPoint = invocation;
        before(invocation.getMethod(), invocation.getArguments(), invocation.getThis());
        return invocation.proceed();
    }
}
