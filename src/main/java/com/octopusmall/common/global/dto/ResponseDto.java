package com.octopusmall.common.global.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseDto {
    // 0表示成功，-1表示失败
    private String resultCode;
    private String resultMsg;
    private Object resultData;

    public ResponseDto() {}

    public ResponseDto(String resultCode, String resultMsg) {
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }

    public static ResponseDto success() {
        return new ResponseDto("0", "success");
    }

    public static ResponseDto fail() {
        return new ResponseDto("-1", "");
    }

}
