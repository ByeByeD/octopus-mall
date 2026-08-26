package com.octopusmall.common.util;

import com.octopusmall.common.exception.OtpsBaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 文件操作工具类
 */
@Slf4j
@Component
public class FileUtil {

    @Value("${file.upload.base-path}")
    private String basePath;

    @Value("${file.upload.avatar-path}")
    private String avatarPath;

    @Value("${file.upload.avatar-max-size}")
    private String avatarMaxSize;

    @Value("${file.upload.avatar-allowed-types}")
    private String avatarTypes;

    /**
     * 上传用户头像
     *
     * @param file   上传的文件
     * @param userId 用户ID
     * @return 文件相对路径，如 /avatar/userId_xxx.png
     */
    public String uploadAvatar(MultipartFile file, String userId) {
        // 1. 校验图片文件
        validateFile(file, avatarMaxSize, avatarTypes);
        // 2. 上传文件通用方法
        return uploadFile(file, userId, avatarPath);
    }

    /**
     * 默认文件上传根目录从file.upload.base-path配置中获取
     * 上传文件通用方法 完整路径：basePath + File.separator + filePath + File.separator + yearMonth
     * @param file 上传的文件
     * @param userId  用户id
     * @param filePath  文件上传路径，该路径填业务的子路径，方法内部会自动拼接完整路径
     * @return 文件相对路径
     */
    private String uploadFile(MultipartFile file, String userId, String filePath) {
        // 2. 获取文件扩展名
        String originalFilename = file.getOriginalFilename();
        String extension = getFileExtension(originalFilename);

        // 3. 生成文件名：userId_时间戳.扩展名
        String fileName = userId + "_" + System.currentTimeMillis() + "." + extension;

        // 4. 确保目录存在
        // 按年月存储文件
        String yearMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM"));

        String completeFilePath = basePath + File.separator + filePath + File.separator + yearMonth;
        File directory = new File(completeFilePath);
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (!created) {
                log.error("创建上传目录失败: {}", completeFilePath);
                throw new OtpsBaseException("创建上传目录失败");
            }
        }

        // 5. 保存文件
        File destFile = new File(completeFilePath, fileName);
        String relativePath = filePath + File.separator + yearMonth + File.separator + fileName;
        try {
            file.transferTo(destFile);
            log.info("上传文件成功: {}, 用户ID: {}", relativePath, userId);
            return relativePath;
        } catch (IOException e) {
            log.error("上传文件失败: {}", e.getMessage());
            throw new OtpsBaseException("上传文件失败");
        }
    }

    /**
     * 校验文件，通用方法
     * @param file 上传的文件
     */
    public void validateFile(MultipartFile file, String fileMaxSize, String fileAllowType) {
        if (file == null || file.isEmpty()) {
            throw new OtpsBaseException("上传文件不能为空");
        }
        
        // 校验文件大小
        long maxSizeBytes = parseSize(fileMaxSize);
        if (file.getSize() > maxSizeBytes) {
            throw new OtpsBaseException("文件大小不能超过 " + fileMaxSize);
        }
        
        // 校验文件类型
        String originalFilename = file.getOriginalFilename();
        String extension = getFileExtension(originalFilename).toLowerCase();
        if (!fileAllowType.contains(extension + ",")) {
            String substring = fileAllowType.substring(0, fileAllowType.length() - 1);
            throw new OtpsBaseException("请上传正确的文件格式: " + substring);
        }
    }

    /**
     * 检查文件是否存在
     *
     * @param relativePath 相对路径，如 /avatar/xxx.png
     * @return 是否存在
     */
    public boolean fileExists(String relativePath) {
        if (CommonUtil.isEmpty(relativePath)) {
            return false;
        }
        String fullPath = basePath + File.separator + relativePath;
        File file = new File(fullPath);
        return file.exists() && file.isFile();
    }

    /**
     * 获取文件字节数组
     *
     * @param relativePath 相对路径
     * @return 文件字节数组
     */
    public byte[] getFile(String relativePath) {
        if (CommonUtil.isEmpty(relativePath)) {
            throw new OtpsBaseException("文件路径不能为空");
        }
        String fullPath = basePath + File.separator + relativePath;
        Path path = Paths.get(fullPath);
        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            log.error("读取文件失败: {}, 错误: {}", fullPath, e.getMessage());
            throw new OtpsBaseException("文件不存在");
        }
    }

    /**
     * 删除文件
     *
     * @param relativePath 相对路径
     * @return 是否删除成功
     */
    public boolean deleteFile(String relativePath) {
        if (CommonUtil.isEmpty(relativePath)) {
            return false;
        }
        String fullPath = basePath + File.separator + relativePath;
        File file = new File(fullPath);
        if (file.exists()) {
            boolean deleted = file.delete();
            if (deleted) {
                log.info("文件删除成功: {}", relativePath);
            }
            return deleted;
        }
        return false;
    }

    /**
     * 获取文件扩展名
     *
     * @param fileName 文件名
     * @return 扩展名（不含点）
     */
    private String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            throw new OtpsBaseException("文件名格式不正确");
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    /**
     * 解析文件大小字符串
     *
     * @param size 大小字符串，如 "5MB"
     * @return 字节数
     */
    private long parseSize(String size) {
        size = size.toUpperCase().trim();
        if (size.endsWith("KB")) {
            return Long.parseLong(size.replace("KB", "").trim()) * 1024;
        } else if (size.endsWith("MB")) {
            return Long.parseLong(size.replace("MB", "").trim()) * 1024 * 1024;
        } else if (size.endsWith("GB")) {
            return Long.parseLong(size.replace("GB", "").trim()) * 1024 * 1024 * 1024;
        } else if (size.endsWith("B")) {
            return Long.parseLong(size.replace("B", "").trim());
        }
        return Long.parseLong(size);
    }

    /**
     * 获取文件MIME类型
     *
     * @param relativePath 相对路径
     * @return MIME类型
     */
    public String getContentType(String relativePath) {
        if (relativePath == null || !relativePath.contains(".")) {
            return "application/octet-stream";
        }
        String extension = getFileExtension(relativePath).toLowerCase();
        switch (extension) {
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "png":
                return "image/png";
            case "gif":
                return "image/gif";
            case "webp":
                return "image/webp";
            default:
                return "application/octet-stream";
        }
    }
}
