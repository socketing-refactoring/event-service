package com.jeein.event.exception;

import lombok.Getter;

@Getter
public class OrderServiceFeignClientException extends RuntimeException {
    private final ErrorCode errorCode;

    public OrderServiceFeignClientException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
