package com.jeein.event.exception;

public class FeignClientParseException extends RuntimeException {
    private final ErrorCode errorCode;

    public FeignClientParseException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
