package com.jeein.event.exception;

import lombok.Getter;

@Getter
public class UploadException extends RuntimeException {
    private final ErrorCode errorCode;

    public UploadException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
