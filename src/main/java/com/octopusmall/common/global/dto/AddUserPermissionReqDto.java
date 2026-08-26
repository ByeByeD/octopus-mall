package com.octopusmall.common.global.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class AddUserPermissionReqDto {

    @NotBlank(message = "用户ID不能为空")
    private String userId;

    @NotEmpty(message = "权限名称列表不能为空")
    private List<@NotBlank(message = "权限名称不能为空") @Size(max = 128, message = "权限名称长度不能超过128") String> permissionNames;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expireTime;
}
