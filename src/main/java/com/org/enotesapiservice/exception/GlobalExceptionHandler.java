package com.org.enotesapiservice.exception;

import com.org.enotesapiservice.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException;

import java.io.FileNotFoundException;
import java.nio.file.AccessDeniedException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<?> handleNullPointerException(NullPointerException e) {
        log.error("NullPointerException : handleNullPointerException : {}", e.getMessage());
        return CommonUtil.createErrorResponseMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException e) {
        log.error("ResourceNotFoundException : handleResourceNotFoundException : {}", e.getMessage());
        return CommonUtil.createErrorResponseMessage(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpServerErrorException.InternalServerError.class)
    public ResponseEntity<?> handleInternalServerErrorException(HttpServerErrorException.InternalServerError e) {
        log.error("InternalServerError : handleInternalServerErrorException : {}", e.getMessage());
        return CommonUtil.createErrorResponseMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        log.error("Exception : handleException : {}", e.getMessage());
        return CommonUtil.createErrorResponseMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException e) {
        log.error("AccessDeniedException : handleAccessDeniedException : {}", e.getMessage());
        return CommonUtil.createErrorResponseMessage(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<?> handleValidationException(ValidationException e) {
        log.error("ValidationException : handleValidationException : {}", e.getMessage());
        return CommonUtil.createErrorResponse(e.getErrors(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExistDataException.class)
    public ResponseEntity<?> handleExistDataException(ExistDataException e) {
        log.error("ExistDataException : handleExistDataException : {}", e.getMessage());
        return CommonUtil.createErrorResponseMessage(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error("HttpMessageNotReadableException : handleHttpMessageNotReadableException : {}", e.getMessage());
        return CommonUtil.createErrorResponseMessage(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<?> handleFileNotFoundException(FileNotFoundException e) {
        log.error("FileNotFoundException : handleFileNotFoundException : {}", e.getMessage());
        return CommonUtil.createErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("GlobalExceptionHandler : handleIllegalArgumentException : {}", e.getMessage());
        return CommonUtil.createErrorResponseMessage(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SuccessException.class)
    public ResponseEntity<?> handleSuccessException(SuccessException e) {
        log.error("SuccessException : handleSuccessException : {}", e.getMessage());
        return CommonUtil.createBuildResponseMessage(e.getMessage(), HttpStatus.OK);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentialsException(BadCredentialsException e) {
        log.error("BadCredentialsException : handleBadCredentialsException : {}", e.getMessage());
        return CommonUtil.createErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
