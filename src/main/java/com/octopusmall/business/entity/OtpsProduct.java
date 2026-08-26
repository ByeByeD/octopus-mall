package com.octopusmall.business.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("otps_product")
public class OtpsProduct {

    @TableId(type = IdType.NONE)
    private String id;

    private String shopId;

    private String name;

    private String category;

    private BigDecimal price;

    private Integer stock;

    private String imageUrl;

    private String remark;

    private String status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
