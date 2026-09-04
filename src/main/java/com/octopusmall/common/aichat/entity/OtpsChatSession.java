package com.octopusmall.common.aichat.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("otps_chat_session")
public class OtpsChatSession {
    @TableId(type=IdType.NONE)
    private String id;
    private String userId;
    private String name;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
