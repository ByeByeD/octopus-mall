package com.octopusmall.common.handler;

import com.octopusmall.common.annotation.IgnoreLoginValid;
import com.octopusmall.common.exception.OtpsBaseException;
import com.octopusmall.common.util.CommonUtil;
import com.octopusmall.common.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;

/**
 * 检查用户token是否合法，并将用户token和userId放入该次请求中，方便后续全局调用，该AOP对所有的controller生效
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
@Order(1)   //优先执行：登录校验
public class UserLoginValidAspect {
    private final JwtUtil jwtUtil;

    private final StringRedisTemplate stringRedisTemplate;

    // @Value 取值，冒号后面是默认值，yml没配置时生效
    @Value("${auth.redis.jwt-black-prefix}")
    private String jwtBlackPrefix;

    //拦截所有Controller类内部的public方法
    @Pointcut("execution(* com.octopusmall..controller..*.*(..))")
    public void pointCut() {}

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        IgnoreLoginValid ignoreLoginValid = method.getAnnotation(IgnoreLoginValid.class);
        if (ignoreLoginValid == null) { // 没有该注解，说明需要进行登录校验
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes == null) {
                throw new OtpsBaseException("无法获取请求上下文");
            }
            HttpServletRequest request = attributes.getRequest();

            String header = request.getHeader("Authorization");
            if (CommonUtil.isEmpty(header) || !header.startsWith("Bearer ")) {
                throw new OtpsBaseException("请登录");
            }
            String token = header.substring(7);

            // jwt黑名单校验
            String blackKey = jwtBlackPrefix + token;
            if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(blackKey))) {
                throw new OtpsBaseException("token已失效，请重新登录");
            }

            Claims claims = jwtUtil.parseToken(token);
            if (claims == null) {
                throw new OtpsBaseException("token无效或已过期，请重新登录");
            }
            String loginUserId = claims.get("userId", String.class);

            request.setAttribute("userId", loginUserId);
            request.setAttribute("token", token);
        }

        return joinPoint.proceed();
    }
}
