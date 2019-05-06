package com.hb.spring.framework.aop;

import com.hb.spring.framework.aop.intercept.HBMethodInvocation;
import com.hb.spring.framework.aop.support.HBAdvisedSupport;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

/**
 * @program: Spring
 * @description:
 * @author: bobobo
 * @create: 2019-05-06 20:17
 **/
public class HBJdkDynamicAopProxy implements HBAopProxy, InvocationHandler {

    private HBAdvisedSupport advised;

    public HBJdkDynamicAopProxy(HBAdvisedSupport advised) {
        this.advised = advised;
    }

    public Object getProxy() {
        return getProxy(this.advised.getTargetClass().getClassLoader());
    }

    public Object getProxy(ClassLoader classLoader) {
        return Proxy.newProxyInstance(classLoader, this.advised.getTargetClass().getInterfaces(),this);
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        List<Object> interceptorsAndDynamicMethodMatchers =
                this.advised.getInterceptorsAndDynamicInterceptionAdvice(method,this.advised.getTargetClass());
        HBMethodInvocation invocation = new HBMethodInvocation(proxy,this.advised.getTarget(),method,args,this.advised.getTargetClass(),interceptorsAndDynamicMethodMatchers);
        return invocation.proceed();
    }
}
