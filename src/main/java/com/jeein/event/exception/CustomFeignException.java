package com.jeein.event.exception;

import lombok.Getter;

@Getter
public class CustomFeignException extends RuntimeException {

    private ErrorCode errorCode;
    private final String code;
    private final String message;
    private final Object data;

    public CustomFeignException(String code, String message, Object data) {
        super(message);
        this.code = code;
        this.message = message;
        this.data = data;
    }
}
