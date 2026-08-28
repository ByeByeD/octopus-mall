package com.octopusmall.common.global.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("otps_dict")
public class OtpsDict {
    @TableId(type = IdType.NONE)
    private String dictId;
    private String enumCode;
    private String enumName;
    private String sortOrder;
    private String isEnabled;
    private String remark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
