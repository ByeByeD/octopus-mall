package com.octopusmall.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 忽略登录校验，如登录接口以及不需要用户登录就可以请求的接口
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)  //关键：运行时保留，反射才能读取
@Documented
public @interface IgnoreLoginValid {
}
