package com.octopusmall.common.global.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.octopusmall.common.exception.OtpsBaseException;
import com.octopusmall.common.global.dto.AddUserPermissionReqDto;
import com.octopusmall.common.global.dto.DeleteUserPermissionReqDto;
import com.octopusmall.common.global.entity.OtpsUser;
import com.octopusmall.common.global.entity.OtpsUserPermission;
import com.octopusmall.common.global.mapper.OtpsUserMapper;
import com.octopusmall.common.global.mapper.OtpsUserPermissionMapper;
import com.octopusmall.common.global.service.OtpsUserPermissionService;
import com.octopusmall.common.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OtpsUserPermissionServiceImpl implements OtpsUserPermissionService {

    private final OtpsUserPermissionMapper otpsUserPermissionMapper;
    private final OtpsUserMapper otpsUserMapper;
    private final StringRedisTemplate stringRedisTemplate;

    @Value("${auth.redis.user-perm-prefix}")
    private String userPermPrefix;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addUserPermissions(AddUserPermissionReqDto reqDto) {
        // 校验用户是否存在（业务校验）
        OtpsUser user = otpsUserMapper.selectOne(new QueryWrapper<OtpsUser>()
                .eq("id", reqDto.getUserId())
                .eq("is_deleted", 0));
        if (user == null) {
            throw new OtpsBaseException("用户不存在");
        }

        // @Valid 已校验非空，直接使用
        List<String> permissionNames = reqDto.getPermissionNames();

        // 去重
        List<String> distinctPermissions = permissionNames.stream()
                .distinct()
                .collect(Collectors.toList());

        // 查询已存在的权限
        List<String> existPermissions = otpsUserPermissionMapper.selectExistPermissionNames(
                reqDto.getUserId(), distinctPermissions);

        // 过滤掉已存在的权限
        Set<String> existSet = existPermissions.stream().collect(Collectors.toSet());
        List<String> toAddPermissions = distinctPermissions.stream()
                .filter(p -> !existSet.contains(p))
                .collect(Collectors.toList());

        if (toAddPermissions.isEmpty()) {
            throw new OtpsBaseException("所有权限均已存在，无需重复添加");
        }

        // 构造权限记录列表
        LocalDateTime now = LocalDateTime.now();
        List<OtpsUserPermission> permissions = new ArrayList<>();
        for (String permissionName : toAddPermissions) {
            OtpsUserPermission permission = new OtpsUserPermission();
            permission.setId(CommonUtil.getSnowID());
            permission.setUserId(reqDto.getUserId());
            permission.setPermissionName(permissionName);
            permission.setCreateTime(now);
            permission.setExpireTime(reqDto.getExpireTime());
            permissions.add(permission);
        }

        // 批量插入权限记录
        int rows = otpsUserPermissionMapper.batchInsertUserPermissions(permissions);
        if (rows != toAddPermissions.size()) {
            throw new OtpsBaseException("批量添加用户权限失败");
        }

        // 刷新用户权限缓存（如果存在）
        refreshUserPermissionCache(reqDto.getUserId());

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteUserPermissions(DeleteUserPermissionReqDto reqDto) {
        // @Valid 已校验非空，直接使用
        List<String> permissionNames = reqDto.getPermissionNames();

        // 去重
        List<String> distinctPermissions = permissionNames.stream()
                .distinct()
                .collect(Collectors.toList());

        // 查询要删除的权限中实际存在的
        List<String> existPermissions = otpsUserPermissionMapper.selectExistPermissionNames(
                reqDto.getUserId(), distinctPermissions);

        if (existPermissions.isEmpty()) {
            throw new OtpsBaseException("所有权限均不存在");
        }

        // 批量删除权限记录
        int rows = otpsUserPermissionMapper.batchDeleteUserPermissions(
                reqDto.getUserId(), existPermissions);
        if (rows != existPermissions.size()) {
            throw new OtpsBaseException("批量删除用户权限失败");
        }

        // 刷新用户权限缓存（如果存在）
        refreshUserPermissionCache(reqDto.getUserId());

        return true;
    }

    /**
     * 刷新用户权限缓存，如果存在则删除，下次访问时重新加载
     */
    private void refreshUserPermissionCache(String userId) {
        String cacheKey = userPermPrefix + userId;
        if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(cacheKey))) {
            stringRedisTemplate.delete(cacheKey);
        }
    }
}
