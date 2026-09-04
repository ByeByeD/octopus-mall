package com.octopusmall.common.util;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 通用工具类
 */
@Component
public class CommonUtil {
    /**
     * 注意@Value注解不能用于static修饰的字段
     * @Value 是 Spring 容器的注入能力，只作用于Spring Bean 对象的成员变量，不处理 static 静态变量。
     * 执行顺序：
     * JVM 加载类 → 执行 static 静态代码块（此时 Spring 容器还没启动，yml 配置还没加载，@Value完全无效）
     * Spring 启动，扫描@Component，new 出 CommonUtil 对象实例
     * Spring 对这个对象实例做属性注入：把 yml 的值赋值给对象的普通成员变量 private long workId
     * 执行@PostConstruct初始化方法，此时才能拿到已经注入好的配置值
     * 之后才可以初始化 Snowflake 实例
     */
    @Value("${snowflake.work-id:1}")
    private long workId;

    @Value("${snowflake.data-center-id:1}")
    private long dataCenterId;

    private static Snowflake SNOWFLAKE;

    @PostConstruct
    public void init() {
//        SNOWFLAKE = IdUtil.createSnowflake(workId, dataCenterId); 这个方法过时了，所以用最新方法
        // 新方法，每次获取的对象是同一个
        SNOWFLAKE = IdUtil.getSnowflake(workId, dataCenterId);
    }

    private CommonUtil() {
    }

    /**
     * 获取固定20位长度雪花ID字符串，左侧补0
     * @return 20位数字字符串
     */
    public static String getSnowID() {
        long id = SNOWFLAKE.nextId();
        String s = String.valueOf(id);
        int length = 20;
        if (s.length() > length) {
            s = s.substring(0, length);
        }
        else {
            s = s + "0".repeat(length - s.length());
        }
        return s;
    }

    public static String getUserId() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            return (String) request.getAttribute("userId");
        }
        return null;
    }

    public static String getToken() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            return (String) request.getAttribute("token");
        }
        return null;
    }

    public static boolean isEmpty(String s) {
        return s == null || s.isBlank();
    }
}