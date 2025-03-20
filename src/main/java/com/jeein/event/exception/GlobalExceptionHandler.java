package com.jeein.event.exception;

import com.fasterxml.jackson.core.JsonParseException;
import com.jeein.event.dto.CommonResponse;

import java.io.IOException;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<CommonResponse<Object>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e) {
        CommonResponse<Object> response =
                CommonResponse.error(ErrorCode.INVALID_REQUEST_VALUE, e.getBindingResult());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomValidationException.class)
    protected ResponseEntity<CommonResponse<Object>> handleCustomValidationException(
            CustomValidationException e) {
        CommonResponse<Object> response =
                CommonResponse.error(ErrorCode.INVALID_REQUEST_VALUE, e.getBindingResult());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<CommonResponse<Object>> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException e) {
        CommonResponse<Object> response = CommonResponse.error(e);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    protected ResponseEntity<CommonResponse<Object>> handleMethodArgumentTypeMismatchException(
            HttpMediaTypeNotSupportedException he) {
        log.error(he.getMessage());
        CommonResponse<Object> response =
                CommonResponse.error(ErrorCode.MULTIPART_NO_BOUNDARY);
        return new ResponseEntity<>(response, ErrorCode.INVALID_CONTENT_TYPE.getStatus());
    }

    @ExceptionHandler(MultipartException.class)
    protected ResponseEntity<CommonResponse<Object>> handleMultipartException(
            MultipartException e) {
        CommonResponse<Object> response =
                CommonResponse.error(ErrorCode.PAYLOAD_TOO_LARGE);
        return new ResponseEntity<>(response, ErrorCode.MULTIPART_NO_BOUNDARY.getStatus());
    }

    @ExceptionHandler(EventException.class)
    protected ResponseEntity<CommonResponse<Object>> handleEventAlreadyExistsException(
            EventException ee) {
        CommonResponse<Object> response =
                CommonResponse.error(ee.getErrorCode());
        return new ResponseEntity<>(response, ee.getErrorCode().getStatus());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<CommonResponse<Object>> handleConstraintViolationException(
            ConstraintViolationException e) {
        log.error(e.getMessage());
        CommonResponse<Object> response =
                CommonResponse.error(ErrorCode.INVALID_ENTITY_VALUE);
        return new ResponseEntity<>(response, ErrorCode.INVALID_ENTITY_VALUE.getStatus());
    }

    @ExceptionHandler(DatabaseConstraintException.class)
    protected ResponseEntity<CommonResponse<Object>> handleDatabaseConstraintException(
            DatabaseConstraintException e) {
        log.error(e.getMessage());
        CommonResponse<Object> response =
                CommonResponse.error(e.getErrorCode());
        return new ResponseEntity<>(response, e.getErrorCode().getStatus());
    }

    @ExceptionHandler(UploadException.class)
    protected ResponseEntity<CommonResponse<Object>> handleUploadException(UploadException e) {
        CommonResponse<Object> response = CommonResponse.error(e.getErrorCode());
        return new ResponseEntity<>(response, e.getErrorCode().getStatus());
    }

    @ExceptionHandler(JsonParseException.class)
    protected ResponseEntity<CommonResponse<Object>> handleJsonParseException(JsonParseException e) {
        log.error(e.getMessage());
        CommonResponse<Object> response = CommonResponse.error(ErrorCode.REQUEST_MAPPING_ERROR);
        return new ResponseEntity<>(response, ErrorCode.REQUEST_MAPPING_ERROR.getStatus());
    }

    @ExceptionHandler(IllegalStateException.class)
    protected ResponseEntity<CommonResponse<Object>> handleIllegalStateException(IllegalStateException e) {
        log.error(e.getMessage());
        CommonResponse<Object> response = CommonResponse.error(ErrorCode.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(response, ErrorCode.INTERNAL_SERVER_ERROR.getStatus());
    }

    @ExceptionHandler(IOException.class)
    protected ResponseEntity<CommonResponse<Object>> handleIOException(IOException e) {
        log.error(e.getMessage());
        CommonResponse<Object> response = CommonResponse.error(ErrorCode.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(response, ErrorCode.INTERNAL_SERVER_ERROR.getStatus());
    }
}
