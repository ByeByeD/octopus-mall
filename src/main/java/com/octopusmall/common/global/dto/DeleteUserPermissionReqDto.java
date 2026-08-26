package com.octopusmall.common.global.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class DeleteUserPermissionReqDto {

    @NotBlank(message = "用户ID不能为空")
    private String userId;

    @NotEmpty(message = "权限名称列表不能为空")
    private List<@NotBlank(message = "权限名称不能为空") String> permissionNames;
}
