package com.octopusmall.common.global.dto;

import lombok.Data;

@Data
public class LoginRespDto {
    private String token;
    private String userId;
    private String nickname;
    private String userType;
}
