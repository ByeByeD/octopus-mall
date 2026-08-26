package com.octopusmall.business.dto;

import lombok.Data;

@Data
public class QueryShopReqDto {

    private String id;

    private String userId;

    private String shopName;

    private String category;

    private String auditStatus;

    private String shopStatus;

    private String province;

    private String city;
}
