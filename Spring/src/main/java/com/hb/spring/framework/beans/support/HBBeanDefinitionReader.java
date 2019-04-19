package com.hb.spring.framework.beans.support;

import com.hb.spring.framework.beans.config.HBBeanDefinition;
import jdk.nashorn.internal.objects.annotations.Getter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @program: Spring
 * @description: 具体的读取配置信息类
 * @author: bobobo
 * @create: 2019-04-19 17:09
 **/
public class HBBeanDefinitionReader {

    /**
     * 储存扫描到的类名称
     */
    private List<String> registyBeanCliasses = new ArrayList<String>();

    /**
     * 根据location拿到的properties
     */
    private Properties config = new Properties();

    //固定配置文件中的key，相当于xml的规范
    private final String SCAN_PACKAGE = "scanPackage";

    public HBBeanDefinitionReader(String[] locations) {
        //通过包定位找到其所对应的文件，转完成为文件流
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(locations[0].replace("classpath:",""));
        try {
            config.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        doScanner(config.getProperty(SCAN_PACKAGE));
    }

    /**
     * 扫描包，获取类
     * @param scanPackage 包名如：com.xx.xx
     */
    private void doScanner(String scanPackage) {
        URL url = this.getClass().getClassLoader().getResource("/" + scanPackage.replace("\\.", "/"));
        File classPath = new File(url.getFile());
        for (File file : classPath.listFiles()) {
            if (file.isDirectory()) {
                doScanner(scanPackage + '.' +file.getName());
            } else {
                if (!file.getName().endsWith(".class")) {continue;}
                String className = (scanPackage + "." + file.getName().replace(".class", ""));
                registyBeanCliasses.add(className);
            }
        }
    }

    /**
     * 把配置文件中扫描到的所有配置信息转换为BeanDefinition对象，方便以后IOC操作
     * @return
     */
    public List<HBBeanDefinition> loadBeanDefinitions() {
        List<HBBeanDefinition> result = new ArrayList<HBBeanDefinition>();

        for (String className : registyBeanCliasses) {
            try {
                Class<?> beanClass = Class.forName(className);

                //如果是一个接口，则不能实例化
                //需要用它的实现类来实例
                if (beanClass.isInterface()) {continue;}

                //beanName有三种情况
                //1、默认是类名首字母小写
                //2、自定义名字
                //3、接口注入
                result.add(doCreateBeanDefinition(toLowerFirstCase(beanClass.getSimpleName()), beanClass.getName()));

                Class<?> [] interfaces = beanClass.getInterfaces();
                for (Class<?> i : interfaces) {
                    //如果是多个实现类，只能覆盖
                    //为什么？因为Spring没那么智能，就是这么傻
                    //这个时候，可以自定义名字
                    result.add(doCreateBeanDefinition(i.getName(),beanClass.getName()));
                }

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }


        return null;
    }

    public Properties getConfig() {
        return config;
    }

    /**
     * 将加载的类解析成一个BeanDefinition
     * @param factoryBeanName 如：com.hb.Haha 就会是haha
     * @param beanClassName 如：com.hb.Haha 全类名
     * @return
     */
    private HBBeanDefinition doCreateBeanDefinition(String factoryBeanName, String beanClassName) {
        HBBeanDefinition beanDefinition = new HBBeanDefinition();
        beanDefinition.setBeanClassName(beanClassName);
        beanDefinition.setFactoryBeanName(factoryBeanName);
        return beanDefinition;
    }

    /**
     * 将驼峰命名的类名 首字母变为小写
     * @param simpleName
     * @return
     */
    private String toLowerFirstCase(String simpleName) {
        char [] chars = simpleName.toCharArray();
        //之所以加，是因为大小写字母的ASCII码相差32，
        // 而且大写字母的ASCII码要小于小写字母的ASCII码
        //在Java中，对char做算学运算，实际上就是对ASCII码做算学运算
        chars[0] += 32;
        return String.valueOf(chars);
    }

}
