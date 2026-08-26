package com.octopusmall.common.global.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.octopusmall.common.global.entity.OtpsUserPermission;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface OtpsUserPermissionMapper extends BaseMapper<OtpsUserPermission> {

    /**
     * 查询用户有效的权限列表
     */
    List<String> selectUserValidPermissionList(@Param("userId") String userId);

    /**
     * 批量插入用户权限记录
     * @param permissions 权限记录列表
     * @return 插入数量
     */
    int batchInsertUserPermissions(@Param("permissions") List<OtpsUserPermission> permissions);

    /**
     * 批量删除用户权限记录
     * @param userId 用户ID
     * @param permissionNames 权限名称集合
     * @return 删除数量
     */
    int batchDeleteUserPermissions(@Param("userId") String userId, @Param("permissionNames") List<String> permissionNames);

    /**
     * 检查用户权限是否存在
     */
    OtpsUserPermission selectUserPermission(@Param("userId") String userId, @Param("permissionName") String permissionName);

    /**
     * 查询用户已存在的权限名称列表
     * @param userId 用户ID
     * @param permissionNames 权限名称集合
     * @return 已存在的权限名称列表
     */
    List<String> selectExistPermissionNames(@Param("userId") String userId, @Param("permissionNames") List<String> permissionNames);
}
