package com.octopusmall.common.handler;

import com.octopusmall.common.annotation.Permission;
import com.octopusmall.common.global.mapper.OtpsUserPermissionMapper;
import com.octopusmall.common.util.CommonUtil;
import com.octopusmall.common.util.JwtUtil;
import io.jsonwebtoken.Claims;
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
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 接口权限AOP
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
@Order(2)  // 在用户登录校验AOP后执行
// 切入点是Permission注解，有该注解的方法才会被拦截
public class PermissionAspect {

    private final JwtUtil jwtUtil;
    private final OtpsUserPermissionMapper otpsUserPermissionMapper;
    private final StringRedisTemplate stringRedisTemplate;

    @Value("${auth.redis.user-perm-prefix}")
    private String userPermPrefix;

    // 切入点是Permission注解，有该注解的方法才会被拦截
    @Pointcut("@annotation(com.octopusmall.common.annotation.Permission)")
    public void pointCut() {}

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        String token = CommonUtil.getToken();
        String loginUserId = CommonUtil.getUserId();
        Claims claims = jwtUtil.parseToken(token);
        long remainMs = jwtUtil.getRemainExpireMs(claims);

        // 获取切入点注释的Permission的name值
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Permission permissionAnnotation = method.getAnnotation(Permission.class);
        String requirePerm = permissionAnnotation.name();

        //读取redis权限缓存
        String permKey = userPermPrefix + loginUserId;
        Set<String> userPermSet = stringRedisTemplate.opsForSet().members(permKey);
        if(userPermSet == null || userPermSet.isEmpty()){
            List<String> dbPermList = otpsUserPermissionMapper.selectUserValidPermissionList(loginUserId);
            SetOperations<String,String> setOps = stringRedisTemplate.opsForSet();
            for(String p : dbPermList){
                setOps.add(permKey, p);
            }
            stringRedisTemplate.expire(permKey, remainMs, TimeUnit.MILLISECONDS);
            userPermSet = Set.copyOf(dbPermList);
        }

        if(!userPermSet.contains(requirePerm)){
            throw new RuntimeException("权限不足");
        }

        return joinPoint.proceed();
    }
}
