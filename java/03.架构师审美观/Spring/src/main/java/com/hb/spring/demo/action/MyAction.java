package com.hb.spring.demo.action;

import com.hb.spring.demo.service.IModifyService;
import com.hb.spring.demo.service.IQueryService;
import com.hb.spring.framework.annotation.HBAutowired;
import com.hb.spring.framework.annotation.HBController;
import com.hb.spring.framework.annotation.HBRequestMapping;
import com.hb.spring.framework.annotation.HBRequestParam;
import com.hb.spring.framework.webmvc.servlet.HBModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: Spring
 * @description:
 * @author: bobobo
 * @create: 2019-04-20 11:54
 **/
@HBController
@HBRequestMapping("/web")
public class MyAction {

    @HBAutowired
    IQueryService queryService;
    @HBAutowired
    IModifyService modifyService;

    @HBRequestMapping("/query.json")
    public HBModelAndView query(HttpServletRequest request, HttpServletResponse response,
                                @HBRequestParam("name") String name){
        String result = queryService.query(name);
        return out(response,result);
    }

    @HBRequestMapping("/add*.json")
    public HBModelAndView add(HttpServletRequest request,HttpServletResponse response,
                              @HBRequestParam("name") String name,@HBRequestParam("addr") String addr){
        String result = null;
        try {
            result = modifyService.add(name,addr);
            return out(response,result);
        } catch (Exception e) {
//			e.printStackTrace();
            Map<String,Object> model = new HashMap<String,Object>();
            model.put("detail",e.getCause().getMessage());
//			System.out.println(Arrays.toString(e.getStackTrace()).replaceAll("\\[|\\]",""));
            model.put("stackTrace", Arrays.toString(e.getStackTrace()).replaceAll("\\[|\\]",""));
            return new HBModelAndView("500",model);
        }

    }

    @HBRequestMapping("/remove.json")
    public HBModelAndView remove(HttpServletRequest request, HttpServletResponse response,
                                 @HBRequestParam("id") Integer id){
        String result = modifyService.remove(id);
        return out(response,result);
    }

    @HBRequestMapping("/edit.json")
    public HBModelAndView edit(HttpServletRequest request,HttpServletResponse response,
                               @HBRequestParam("id") Integer id,
                               @HBRequestParam("name") String name){
        String result = modifyService.edit(id,name);
        return out(response,result);
    }

    private HBModelAndView out(HttpServletResponse resp,String str){
        try {
            resp.getWriter().write(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
