package com.hb.spring.framework.annotation;

import java.lang.annotation.*;

/**
 * @program: Spring
 * @description:
 * @author: bobobo
 * @create: 2019-04-20 11:39
 **/
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HBAutowired {
    String value() default "";
}
