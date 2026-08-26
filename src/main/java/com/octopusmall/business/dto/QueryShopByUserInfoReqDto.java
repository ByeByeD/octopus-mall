package com.octopusmall.business.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class QueryShopByUserInfoReqDto {

    @Size(max = 20, message = "账号长度不能超过20位")
    private String account;

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式错误")
    private String phoneNumber;
}
