package com.octopusmall.common.global.controller;

import com.octopusmall.common.annotation.IgnoreLoginValid;
import com.octopusmall.common.global.dto.FileUploadRespDto;
import com.octopusmall.common.global.dto.ResponseDto;
import com.octopusmall.common.util.FileUtil;
import com.octopusmall.common.util.CommonUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件处理控制器
 */
@Slf4j
@RestController
@RequestMapping("/common/file")
@RequiredArgsConstructor
public class FileController {

    private final FileUtil fileUtil;

    /**
     * 上传用户头像
     *
     * @param file 上传的图片文件
     * @return 文件上传结果
     */
    @PostMapping("/upload/avatar")
    public ResponseDto uploadAvatar(@RequestParam("file") MultipartFile file) {
        String userId = CommonUtil.getUserId();
        log.info("收到头像上传请求，用户ID: {}, 文件名: {}", userId, file.getOriginalFilename());
        
        // 上传文件
        String relativePath = fileUtil.uploadAvatar(file, userId);
        
        // 构建响应
        FileUploadRespDto respDto = new FileUploadRespDto();
        respDto.setFilePath(relativePath);
        respDto.setFileName(file.getOriginalFilename());
        respDto.setFileSize(file.getSize());

        ResponseDto responseDto = new ResponseDto("0", "success");
        responseDto.setResultData(respDto);
        return responseDto;
    }

    /**
     * 获取文件
     *
     * @return 图片字节数组
     */
    @IgnoreLoginValid
    @GetMapping("/getFile/**")
    public ResponseEntity<byte[]> getFile(HttpServletRequest request) {
        // 截取 /common/file/getFile/ 后面全部路径
        String subSeparator = "/common/file/getFile/";
        String path = request.getRequestURI().substring(request.getRequestURI().indexOf(subSeparator) + subSeparator.length());
        log.info("获取文件请求: {}", path);
        byte[] fileBytes = fileUtil.getFile(path);
        // org.springframework.http.MediaType
        MediaType mediaType = MediaTypeFactory.getMediaType(path).orElse(MediaType.APPLICATION_OCTET_STREAM);
        String contentType = mediaType.toString();
        // 使用spring提供的能力，不用自己判断
//        String contentType = fileUtil.getContentType(path);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(contentType));
        headers.setCacheControl("public, max-age=31536000"); // 缓存1年

        return new ResponseEntity<>(fileBytes, headers, HttpStatus.OK);
    }
}
