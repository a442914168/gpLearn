package com.hb.spring.framework.context;

import com.hb.spring.framework.annotation.HBAutowired;
import com.hb.spring.framework.annotation.HBController;
import com.hb.spring.framework.annotation.HBService;
import com.hb.spring.framework.beans.HBBeanWrapper;
import com.hb.spring.framework.beans.config.HBBeanDefinition;
import com.hb.spring.framework.beans.config.HBBeanPostProcessor;
import com.hb.spring.framework.beans.support.HBBeanDefinitionReader;
import com.hb.spring.framework.beans.support.HBDefaultListableBeanFactory;
import com.hb.spring.framework.core.HBBeanFactory;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: Spring
 * @description: 程序入口
 * @author: bobobo
 * @create: 2019-04-19 17:03
 **/
public class HBApplicationContext extends HBDefaultListableBeanFactory implements HBBeanFactory {

    //文件的地址
    private String[] configLoactions;
    private HBBeanDefinitionReader reader;

    //单例的IOC容器缓存
    private Map<String, Object> singletonObjects = new ConcurrentHashMap<String, Object>();
    //通用的IOC容器
    private Map<String, HBBeanWrapper> factoryBeanInstanceCache = new ConcurrentHashMap<String, HBBeanWrapper>();

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

    /**
     * 将非延迟加载的类，进行初始化
     */
    private void doAutowrited() {
        for (Map.Entry<String, HBBeanDefinition> beanDefinitionEntry : super.beanDefinitionMap.entrySet()) {
            String beanName = beanDefinitionEntry.getKey();
            if (!beanDefinitionEntry.getValue().isLazyInit()) {
                try {
                    getBean(beanName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 注册，把配置信息放到容器里面（伪IOC容器）,到此为止 容器初始化完毕
     *
     * @param beanDefinitions 储存找到的配置文件信息
     */
    private void doRegisterBeanDefinition(List<HBBeanDefinition> beanDefinitions) throws Exception {
        for (HBBeanDefinition beanDefinition : beanDefinitions) {
            if (super.beanDefinitionMap.containsKey(beanDefinition.getFactoryBeanName())) {
                throw new Exception("The “" + beanDefinition.getFactoryBeanName() + "” is exists!!");
            }
            super.beanDefinitionMap.put(beanDefinition.getFactoryBeanName(), beanDefinition);
        }
    }

    public Object getBean(Class<?> beanClass) throws Exception {
        return getBean(beanClass.getName());
    }

    /**
     * 依赖注入，通过BeanDefinition中的信息反射创建一个实例返回
     * Spring的做法是，不会把最原始的对象放出去，而是使用一个BeanWrapper来进行一次包装
     * 装饰器模式：
     * 1、保留原来的oop关系
     * 2、需要对它进行拓展，增强（为AOP打基础）
     *
     * @param beanName
     * @return
     * @throws Exception
     */
    public Object getBean(String beanName) throws Exception {

        HBBeanDefinition hbBeanDefinition = this.beanDefinitionMap.get(beanName);
        Object instance = null;

        HBBeanPostProcessor postProcessor = new HBBeanPostProcessor();
        postProcessor.postProcessBeforeInitialization(instance, beanName);

        instance = instantiateBean(beanName, hbBeanDefinition);

        HBBeanWrapper beanWrapper = new HBBeanWrapper(instance);

        // 将BeanWrapper保存到IOC容器中
        this.factoryBeanInstanceCache.put(beanName, beanWrapper);

        postProcessor.postProcessAfterInitialization(instance, beanName);

        //注入
        populateBean(beanName, new HBBeanDefinition(), beanWrapper);

        return this.factoryBeanInstanceCache.get(beanName).getWrappedInstance();
    }

    /**
     * 将类里面带有注解的 进行注入
     *
     * @param beanName         全类名
     * @param hbBeanDefinition 类的配置信息
     * @param beanWrapper      类的包装信息
     */
    private void populateBean(String beanName, HBBeanDefinition hbBeanDefinition, HBBeanWrapper beanWrapper) {
        Object instance = beanWrapper.getWrappedInstance();

        Class<?> clazz = beanWrapper.getWrappedClass();
        //判断只有加了注解的类，才执行依赖注入
        if (!(clazz.isAnnotationPresent(HBController.class)) || clazz.isAnnotationPresent(HBService.class)) {
            return;
        }

        //获得所有的fields
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(HBAutowired.class)) {
                continue;
            }

            HBAutowired autowired = field.getAnnotation(HBAutowired.class);

            String autowiredBeanName = autowired.value().trim();
            if ("".equals(autowiredBeanName)) {
                autowiredBeanName = field.getType().getName();
            }

            //强制访问
            field.setAccessible(true);

            if (this.factoryBeanInstanceCache.get(autowiredBeanName) == null) {
                continue;
            }

            try {
                field.set(instance, this.factoryBeanInstanceCache.get(autowiredBeanName).getWrappedInstance());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 通过反射 初始化Bean
     * @param beanName 全类名
     * @param hbBeanDefinition 类的配置信息
     * @return
     */
    private Object instantiateBean(String beanName, HBBeanDefinition hbBeanDefinition) {
        //1、拿到要实例化的对象的类名
        String className = hbBeanDefinition.getBeanClassName();

        //2、反射实例化，得到一个对象
        Object instance = null;

        //默认认为就是单例的，细节先不考虑
        if (this.singletonObjects.containsKey(className)) {
            instance = this.singletonObjects.get(className);
        } else {
            try {
                Class<?> clazz = Class.forName(className);
                instance = clazz.newInstance();
                this.singletonObjects.put(className, instance);
                this.singletonObjects.put(hbBeanDefinition.getFactoryBeanName(), instance);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return instance;
    }

    public String[] getBeanDefinitionNames() {
        return this.beanDefinitionMap.keySet().toArray(new String[this.beanDefinitionMap.size()]);
    }

    public Properties getConfig() {
        return this.reader.getConfig();
    }
}
