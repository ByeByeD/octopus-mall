package com.octopusmall.common.exception;

import lombok.Getter;

import java.io.Serial;

/**
 * 业务异常基类，用于替换 RuntimeException
 */
@Getter
public class OtpsBaseException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    private String code = "-1";

    public OtpsBaseException(String message) {
        super(message);
    }

    public OtpsBaseException(String code,String message) {
        super(message);
        this.code = code;
    }

    public OtpsBaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public OtpsBaseException(Throwable cause) {
        super(cause);
    }

    public OtpsBaseException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

}
