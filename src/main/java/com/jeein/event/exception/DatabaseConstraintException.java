package com.jeein.event.exception;

import lombok.Getter;

@Getter
public class DatabaseConstraintException extends RuntimeException {
    private final ErrorCode errorCode;

    public DatabaseConstraintException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
