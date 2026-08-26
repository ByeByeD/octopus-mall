package com.octopusmall.business.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("otps_shop")
public class OtpsShop {

    @TableId(type = IdType.NONE)
    private String id;

    private String userId;

    private String shopName;

    private String province;

    private String city;

    private String detailAddress;

    private String category;

    private String description;

    private String avatarUrl;

    private String contactName;

    private String contactPhone;

    private String licenseNo;

    private String licensePhotoUrl;

    private LocalDate licenseExpireDate;

    private String auditStatus;

    private String auditRemark;

    private LocalDateTime auditTime;

    private String shopStatus;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
