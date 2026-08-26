package com.octopusmall.common.global.dto;

import lombok.Data;

/**
 * 文件上传响应DTO
 */
@Data
public class FileUploadRespDto {
    
    /**
     * 文件相对路径，如 /avatar/user_xxx.png
     */
    private String filePath;
    
    /**
     * 文件名
     */
    private String fileName;
    
    /**
     * 文件大小（字节）
     */
    private Long fileSize;
}
