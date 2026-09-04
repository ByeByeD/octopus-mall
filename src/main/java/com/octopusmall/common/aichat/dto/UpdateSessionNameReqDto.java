package com.octopusmall.common.aichat.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateSessionNameReqDto {
    
    @NotBlank(message = "会话ID不能为空")
    private String sessionId;
    
    @NotBlank(message = "会话名称不能为空")
    @Size(max = 20, message = "会话名称长度不能超过20")
    private String name;
}
