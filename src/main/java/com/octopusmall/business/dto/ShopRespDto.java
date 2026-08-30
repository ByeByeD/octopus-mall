package com.octopusmall.business.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ShopRespDto {

    private String id;

    private String userId;

    private String shopName;

    private String province;

    private String provinceName;

    private String city;

    private String cityName;

    private String detailAddress;

    private String category;

    private String categoryName;

    private String description;

    private String avatarUrl;

    private String contactName;

    private String contactPhone;

    private String licenseNo;

    private String licensePhotoUrl;

    private LocalDate licenseExpireDate;

    private String auditStatus;

    private String auditStatusName;

    private String auditRemark;

    private LocalDateTime auditTime;

    private String shopStatus;

    private String shopStatusName;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
