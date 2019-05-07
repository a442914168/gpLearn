package com.hb.spring.demo.aspect;

import com.hb.spring.framework.aop.aspect.HBJoinPoint;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

/**
 * @program: Spring
 * @description:
 * @author: bobobo
 * @create: 2019-05-07 19:55
 **/
@Slf4j
public class LogAspect {

    //在调用一个方法之前，执行before方法
    public void before(HBJoinPoint joinPoint){
        joinPoint.setUserAttribute("startTime_" + joinPoint.getMethod().getName(),System.currentTimeMillis());
        //这个方法中的逻辑，是由我们自己写的
        log.info("Invoker Before Method!!!" +
                "\nTargetObject:" +  joinPoint.getThis() +
                "\nArgs:" + Arrays.toString(joinPoint.getArguments()));
    }

    //在调用一个方法之后，执行after方法
    public void after(HBJoinPoint joinPoint){
        log.info("Invoker After Method!!!" +
                "\nTargetObject:" +  joinPoint.getThis() +
                "\nArgs:" + Arrays.toString(joinPoint.getArguments()));
        long startTime = (Long) joinPoint.getUserAttribute("startTime_" + joinPoint.getMethod().getName());
        long endTime = System.currentTimeMillis();
        System.out.println("use time :" + (endTime - startTime));
    }

    public void afterThrowing(HBJoinPoint joinPoint, Throwable ex){
        log.info("出现异常" +
                "\nTargetObject:" +  joinPoint.getThis() +
                "\nArgs:" + Arrays.toString(joinPoint.getArguments()) +
                "\nThrows:" + ex.getMessage());
    }

}
