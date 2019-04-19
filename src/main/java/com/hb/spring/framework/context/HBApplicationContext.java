package com.hb.spring.framework.context;

import com.hb.spring.framework.beans.config.HBBeanDefinition;
import com.hb.spring.framework.beans.support.HBBeanDefinitionReader;
import com.hb.spring.framework.beans.support.HBDefaultListableBeanFactory;
import com.hb.spring.framework.core.HBBeanFactory;

import java.util.List;

/**
 * @program: Spring
 * @description: 程序入口
 * @author: bobobo
 * @create: 2019-04-19 17:03
 **/
public class HBApplicationContext extends HBDefaultListableBeanFactory implements HBBeanFactory {

    //文件的地址
    private String [] configLoactions;
    private HBBeanDefinitionReader reader;

    public HBApplicationContext(String... configLoactions) {
        this.configLoactions = configLoactions;
        try {
            refresh();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void refresh() throws Exception {
        //1、 定位，定位配置文件
        reader = new HBBeanDefinitionReader(this.configLoactions);

        //2、 加载配置文件，扫描相关的类，把他们封装成BeanDefinition
        List<HBBeanDefinition> beanDefinitions = reader.loadBeanDefinitions();

        //3、 注册，把配置信息放到容器里面（伪IOC容器）
        doRegisterBeanDefinition(beanDefinitions);

        //4、 把不是延时加载的类，提前初始化
        doAutowrited();
    }

    private void doAutowrited() {

    }

    private void doRegisterBeanDefinition(List<HBBeanDefinition> beanDefinitions) {

    }

    public Object getBean(String beanName) throws Exception {
        return null;
    }
}
