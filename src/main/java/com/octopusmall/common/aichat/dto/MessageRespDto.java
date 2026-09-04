package com.octopusmall.common.aichat.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MessageRespDto {
    private String id;
    private String sessionId;
    private Integer msgIndex;
    /*
        消息类型 值有：
        USER->用户消息
        AI->AI消息
        SYSTEM->系统提示词消息
    */
    private String type;
    private String msgContent;
    private LocalDateTime createTime;
}
