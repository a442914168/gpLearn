package com.hb.spring.framework.webmvc.servlet;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

/**
 * @program: Spring
 * @description:
 * @author: bobobo
 * @create: 2019-05-04 19:20
 **/
public class HBHandlerMapping {

    private Object controller;//方法对应的实例
    private Method method; //映射的方法
    private Pattern pattern; //URL的正则匹配

    public HBHandlerMapping(Object controller, Method method, Pattern pattern) {
        this.controller = controller;
        this.method = method;
        this.pattern = pattern;
    }

    public Object getController() {
        return controller;
    }

    public Method getMethod() {
        return method;
    }

    public Pattern getPattern() {
        return pattern;
    }
}
