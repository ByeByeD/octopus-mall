package com.octopusmall.common.annotation;

import java.lang.annotation.*;

/**
 * 用户权限控制注解，配合AOP使用
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Permission {
    String name();
}
