package com.octopusmall.common.global.dto;

import lombok.Data;

@Data
public class AreaReqDto {
    private String areaCode;
    private String areaName;
    // 层级：1-省份,2-城市,3-区县
    private Integer areaLevel;
    private String parentCode;
}
