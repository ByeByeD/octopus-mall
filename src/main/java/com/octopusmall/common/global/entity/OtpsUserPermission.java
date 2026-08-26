package com.octopusmall.common.global.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("otps_user_permission")
public class OtpsUserPermission {
    @TableId(type = IdType.NONE)
    private String id;
    private String userId;
    private String permissionName;
    private LocalDateTime createTime;
    private LocalDateTime expireTime;
}