package com.octopusmall.common.global.service;

import com.octopusmall.common.global.dto.AddUserPermissionReqDto;
import com.octopusmall.common.global.dto.DeleteUserPermissionReqDto;

public interface OtpsUserPermissionService {

    /**
     * 批量新增用户权限
     * @param reqDto 权限信息（包含权限数组）
     * @return 是否成功
     */
    boolean addUserPermissions(AddUserPermissionReqDto reqDto);

    /**
     * 批量删除用户权限
     * @param reqDto 权限信息（包含权限数组）
     * @return 是否成功
     */
    boolean deleteUserPermissions(DeleteUserPermissionReqDto reqDto);
}
