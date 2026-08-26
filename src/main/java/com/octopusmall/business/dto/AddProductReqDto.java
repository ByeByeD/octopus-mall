package com.octopusmall.business.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class AddProductReqDto {
    @NotBlank(message = "商品名称不能为空")
    @Size(max = 100, message = "商品名称长度不能超过100")
    private String name;

    @NotBlank(message = "店铺ID不能为空")
    private String shopId;

    @NotBlank(message = "商品分类不能为空")
    @Size(max = 20, message = "商品分类长度不能超过20")
    private String category;

    @NotNull(message = "商品价格不能为空")
    @DecimalMin(value = "0.01", message = "商品价格必须大于0")
    @Digits(integer = 18, fraction = 2, message = "商品价格格式不正确")
    private BigDecimal price;

    @NotNull(message = "库存数量不能为空")
    @Min(value = 0, message = "库存数量不能为负数")
    @Max(value = 999999999, message = "库存数量不能超过999999999")
    private Integer stock;

    @Size(max = 200, message = "商品图片路径长度不能超过200")
    private String imageUrl;

    @Size(max = 200, message = "商品简单描述长度不能超过200")
    private String remark;
}
