package com.hb.spring.framework.beans;

/**
 * @program: Spring
 * @description: 加载类的包装，主要用户apo操作
 * @author: bobobo
 * @create: 2019-04-19 17:12
 **/
public class HBBeanWrapper {

    private Object wrappedInstance;
    private Class<?> wrappedClass;

    public HBBeanWrapper(Object wrappedInstance) {
        this.wrappedInstance = wrappedInstance;
    }

    public Object getWrappedInstance() {
        return wrappedInstance;
    }

    /**
     * 返回代理以后的Class
     * @return 可能会是个 $Proxy0
     */
    public Class<?> getWrappedClass() {
        return wrappedClass;
    }
}
