package com.octopusmall.common.global.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateUserInfoReqDto {

    @Size(max = 20, message = "昵称长度不能超过20")
    private String nickname;

    @Pattern(regexp = "^$|^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phoneNumber;

    @Pattern(regexp = "^$|^\\w+@\\w+\\.\\w+$", message = "邮箱格式不正确")
    @Size(max = 300, message = "邮箱长度不能超过300")
    private String email;

    private String userImage;
}
