package com.octopusmall.business.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class QueryShopByAuditStatusReqDto {

    @NotBlank(message = "审核状态不能为空")
    private String auditStatus;
}
