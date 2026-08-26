package com.octopusmall.common.global.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterReqDto {
    @Pattern(regexp = "^[A-Za-z0-9]{6,20}$", message = "账号只能由大小写英文字母、阿拉伯数字组成，长度6‑20位")
    private String account;

    @NotBlank(message = "密码不能为空")
    @Size(min = 8, max = 20, message = "密码长度需要大于8位，不能超过20位")
    private String password;

    @Size(max = 20, message = "昵称长度不能超过20位")
    private String nickname;

    /**
     * 手机号：允许为空；不为空则必须格式正确
     */
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式错误")
    private String phoneNumber;

    /**
     * 邮箱：允许为空；不为空校验格式
     */
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "邮箱格式错误")
    private String email;

    @NotBlank(message = "用户类型不能为空")
    @Size(max = 1, message = "用户类型长度不能超过1位")
    private String type;
}
