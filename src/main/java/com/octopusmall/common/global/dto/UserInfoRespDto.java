package com.octopusmall.common.global.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserInfoRespDto {
    private String id;
    private String account;
    private String nickname;
    private String userImage;
    private String phoneNumber;
    private String email;
    private String type;
    private String status;
    private LocalDateTime createTime;
}