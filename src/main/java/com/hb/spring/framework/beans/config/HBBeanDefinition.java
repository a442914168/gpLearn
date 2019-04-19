package com.hb.spring.framework.beans.config;

/**
 * @program: Spring
 * @description: 储存找到的配置文件信息
 * @author: bobobo
 * @create: 2019-04-19 17:08
 **/
public class HBBeanDefinition {

    /**
     * 如：com.hb.Haha 就会是haha
     */
    private String beanClassName;

    /**
     * 是否懒加载
     */
    private boolean lazyInit = false;

    /**
     * 如：com.hb.Haha 全类名
     */
    private String factoryBeanName;

    public String getBeanClassName() {
        return beanClassName;
    }

    public void setBeanClassName(String beanClassName) {
        this.beanClassName = beanClassName;
    }

    public boolean isLazyInit() {
        return lazyInit;
    }

    public void setLazyInit(boolean lazyInit) {
        this.lazyInit = lazyInit;
    }

    public String getFactoryBeanName() {
        return factoryBeanName;
    }

    public void setFactoryBeanName(String factoryBeanName) {
        this.factoryBeanName = factoryBeanName;
    }
}
