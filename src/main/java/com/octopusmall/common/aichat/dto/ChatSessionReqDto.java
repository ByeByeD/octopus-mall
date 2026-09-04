package com.octopusmall.common.aichat.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChatSessionReqDto {
    
    @NotBlank(message = "会话ID不能为空")
    private String sessionId;
}
