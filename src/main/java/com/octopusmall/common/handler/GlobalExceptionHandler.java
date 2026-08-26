package com.octopusmall.common.handler;

import com.octopusmall.common.exception.OtpsBaseException;
import com.octopusmall.common.global.dto.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * @Valid @RequestBody json参数校验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseDto handleValidException(MethodArgumentNotValidException ex) {
        Map<String,String> errMap = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errMap.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        ResponseDto resp = new ResponseDto();
        resp.setResultCode("-1");
        resp.setResultMsg("参数校验失败");
        resp.setResultData(errMap);
        return resp;
    }

    /**
     * 表单提交 @Valid 校验（非@RequestBody）捕获 BindException
     */
    @ExceptionHandler(BindException.class)
    public ResponseDto handleBindException(BindException ex) {
        Map<String,String> errMap = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errMap.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        ResponseDto resp = new ResponseDto();
        resp.setResultCode("-1");
        resp.setResultMsg("参数校验失败");
        resp.setResultData(errMap);
        return resp;
    }

    /**
     * 业务异常，统一处理
     */
    @ExceptionHandler(OtpsBaseException.class)
    public ResponseDto handleOtpsBaseException(OtpsBaseException e) {
        log.warn("业务异常: {}", e.getMessage());
        ResponseDto resp = new ResponseDto();
        resp.setResultCode("-1");
        resp.setResultMsg(e.getMessage());
        return resp;
    }

    /**
     * 其他运行时异常兜底处理
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseDto handleRuntimeException(RuntimeException e) {
        log.error("系统异常: ", e);
        ResponseDto resp = new ResponseDto();
        resp.setResultCode("-1");
        resp.setResultMsg(e.getMessage());
        return resp;
    }
}