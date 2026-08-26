package com.octopusmall.common.global.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data // @ToString、@EqualsAndHashCode、@Getter / @Setter和@RequiredArgsConstructor 的集合，自动实现这些注解实现的功能
@TableName("otps_user")
public class OtpsUser {

    @TableId(type = IdType.NONE)
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
}