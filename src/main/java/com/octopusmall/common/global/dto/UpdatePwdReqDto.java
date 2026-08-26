package com.octopusmall.common.global.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdatePwdReqDto {
    @NotBlank(message = "旧密码不能为空")
    private String oldPassword;
    @NotBlank(message = "新密码不能为空")
    @Size(min=8, max = 20, message = "密码长度需要大于8位，不能超过20位")
    private String newPassword;
}
