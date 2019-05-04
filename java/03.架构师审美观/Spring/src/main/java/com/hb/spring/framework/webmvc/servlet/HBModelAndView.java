package com.hb.spring.framework.webmvc.servlet;

import java.util.Map;

/**
 * @program: Spring
 * @description:
 * @author: bobobo
 * @create: 2019-05-04 19:22
 **/
public class HBModelAndView {

    private String viewName;
    private Map<String, ?> model;

    public HBModelAndView(String viewName) {
        this.viewName = viewName;
    }

    public HBModelAndView(String viewName, Map<String, ?> model) {
        this.viewName = viewName;
        this.model = model;
    }

    public String getViewName() {
        return viewName;
    }

    public Map<String, ?> getModel() {
        return model;
    }
}
