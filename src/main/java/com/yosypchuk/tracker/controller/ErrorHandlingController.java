package com.yosypchuk.tracker.controller;

import com.yosypchuk.tracker.exception.EntityAlreadyExistException;
import com.yosypchuk.tracker.exception.EntityNotFoundException;
import com.yosypchuk.tracker.model.error.ApiError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ErrorHandlingController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EntityAlreadyExistException.class)
    public ResponseEntity<ApiError> handleEntityAlreadyExistException(EntityAlreadyExistException e) {
        log.error("handleEntityAlreadyExistException: {}", e.getMessage(), e);
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, e.getMessage(), e));
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiError> handleEntityNotFoundException(EntityNotFoundException e) {
        log.error("handleEntityNotFoundException: {}", e.getMessage(), e);
        return buildResponseEntity(new ApiError(HttpStatus.NOT_FOUND, e.getMessage(), e));
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiError> handleAuthenticationException(BadCredentialsException e) {
        log.error("handleAuthenticationException: {}", e.getMessage(), e);
        return buildResponseEntity(new ApiError(HttpStatus.FORBIDDEN, e.getMessage(), e));
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDeniedException(AccessDeniedException e) {
        log.error("handleAccessDenied: {}", e.getMessage(), e);
        return buildResponseEntity(new ApiError(HttpStatus.FORBIDDEN, e.getMessage(), e));
    }

    private ResponseEntity<ApiError> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
