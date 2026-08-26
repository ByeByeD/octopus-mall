package com.octopusmall.common.global.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("otps_user_his")
public class OtpsUserHis {

    @TableId(type = IdType.NONE)
    private String hisId;

    private String id;

    private String account;

    private String password;

    private String nickname;

    private String userImage;

    private String phoneNumber;

    private String email;

    private String type;

    private String status;

    private String auditStatus;

    private String auditRemark;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Integer isDeleted;

    private LocalDateTime hisCreateTime;
}
