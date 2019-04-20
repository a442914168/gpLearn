package com.hb.spring.framework.annotation;

import java.lang.annotation.*;

/**
 * @program: Spring
 * @description:
 * @author: bobobo
 * @create: 2019-04-20 11:38
 **/
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HBController {
    String value() default "";
}
