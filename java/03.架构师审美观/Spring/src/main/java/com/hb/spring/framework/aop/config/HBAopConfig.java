package com.hb.spring.framework.aop.config;

import lombok.Data;

/**
 * @program: Spring
 * @description: Aop配置类
 * @author: bobobo
 * @create: 2019-05-06 20:21
 **/
@Data
public class HBAopConfig {

    private String pointCut;
    private String aspectBefore;
    private String aspectAfter;
    private String aspectClass;
    private String aspectAfterThrow;
    private String aspectAfterThrowingName;

}
