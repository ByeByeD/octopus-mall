package com.octopusmall.common.aichat.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SessionRespDto {
    private String id;
    private String name;
    private LocalDateTime creatTime;    // 注意：实体字段拼写是creatTime
    private LocalDateTime updateTime;
}
