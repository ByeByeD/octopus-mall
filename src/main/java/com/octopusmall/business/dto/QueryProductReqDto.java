package com.octopusmall.business.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class QueryProductReqDto {
    @NotBlank(message = "店铺ID不能为空")
    private String shopId;

    private String id;

    @Size(max = 100, message = "商品名称长度不能超过100")
    private String name;

    @Size(max = 20, message = "商品分类长度不能超过20")
    private String category;

    @Size(max = 10, message = "商品状态长度不能超过10")
    private String status;
}
