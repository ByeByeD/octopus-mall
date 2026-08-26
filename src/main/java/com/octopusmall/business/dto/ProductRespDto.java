package com.octopusmall.business.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ProductRespDto {

    private String id;

    private String shopId;

    private String name;

    private String category;

    private String categoryName;

    private BigDecimal price;

    private Integer stock;

    private String imageUrl;

    private String remark;

    private String status;

    private String statusName;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
