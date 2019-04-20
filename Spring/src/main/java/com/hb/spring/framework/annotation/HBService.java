package com.hb.spring.framework.annotation;

import java.lang.annotation.*;

/**
 * @program: Spring
 * @description:
 * @author: bobobo
 * @create: 2019-04-20 11:39
 **/
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HBService {
    String value() default "";
}
