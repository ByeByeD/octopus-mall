package com.octopusmall.common.aichat.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("otps_chat_message")
public class OtpsChatMessage {
    @TableId(type = IdType.NONE)
    private String id;
    private String sessionId;
    private Integer msgIndex;
    private String msgContent;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
