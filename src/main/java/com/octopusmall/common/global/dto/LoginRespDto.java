package com.octopusmall.common.global.dto;

import lombok.Data;

@Data
public class LoginRespDto extends UserInfoRespDto {
    private String token;
}
