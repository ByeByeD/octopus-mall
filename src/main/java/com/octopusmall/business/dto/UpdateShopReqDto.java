package com.octopusmall.business.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.time.LocalDate;

@Data
public class UpdateShopReqDto {

    @NotBlank(message = "店铺名称不能为空")
    @Size(max = 100, message = "店铺名称长度不能超过100")
    private String shopName;

    @NotBlank(message = "省份不能为空")
    @Size(max = 50, message = "省份长度不能超过50")
    private String province;

    @NotBlank(message = "城市不能为空")
    @Size(max = 50, message = "城市长度不能超过50")
    private String city;

    @NotBlank(message = "详细地址不能为空")
    @Size(max = 200, message = "详细地址长度不能超过200")
    private String detailAddress;

    @NotBlank(message = "店铺分类不能为空")
    @Size(max = 20, message = "店铺分类长度不能超过20")
    private String category;

    @Size(max = 500, message = "店铺简介长度不能超过500")
    private String description;

    @Size(max = 200, message = "店铺头像路径长度不能超过200")
    private String avatarUrl;

    @NotBlank(message = "联系人姓名不能为空")
    @Size(max = 50, message = "联系人姓名长度不能超过50")
    private String contactName;

    @NotBlank(message = "联系人电话不能为空")
    @Size(max = 20, message = "联系人电话长度不能超过20")
    private String contactPhone;

    @NotBlank(message = "店铺状态不能为空")
    @Size(max = 10, message = "店铺状态长度不能超过10")
    private String shopStatus;

    @NotBlank(message = "营业执照编号不能为空")
    @Size(max = 50, message = "营业执照编号长度不能超过50")
    private String licenseNo;

    @NotBlank(message = "营业执照照片不能为空")
    @Size(max = 200, message = "营业执照照片路径长度不能超过200")
    private String licensePhotoUrl;

    private LocalDate licenseExpireDate;
}
