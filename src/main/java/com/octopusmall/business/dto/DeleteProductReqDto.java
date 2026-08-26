package com.octopusmall.business.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DeleteProductReqDto {

    @NotBlank(message = "商品ID不能为空")
    private String id;

    @NotBlank(message = "店铺ID不能为空")
    private String shopId;
}
