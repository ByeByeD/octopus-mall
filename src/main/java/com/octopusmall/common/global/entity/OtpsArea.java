package com.octopusmall.common.global.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("otps_area")
public class OtpsArea {
    @TableId(type = IdType.NONE)
    private String id;
    private String areaCode;
    private String areaName;
    //层级：1-省份,2-城市,3-区县
    private Integer areaLevel;
    private String parentCode;
    private LocalDateTime creatTime;
    private LocalDateTime updateTime;
}
