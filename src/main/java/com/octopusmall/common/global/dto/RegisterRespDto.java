package com.octopusmall.common.global.dto;

import lombok.Data;

@Data
public class RegisterRespDto {
    private String userId;
    private String account;
    private String type;
    private String nickname;
}
