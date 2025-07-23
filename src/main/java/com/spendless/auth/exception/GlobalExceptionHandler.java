package com.spendless.auth.exception;

import com.spendless.auth.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handle custom ResourceNotFoundException
    @ExceptionHandler(ConfigDataResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ConfigDataResourceNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    // Handle invalid URLs (non-existent endpoints)
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<String> handleNoHandlerFoundException(NoHandlerFoundException ex) {
        return new ResponseEntity<>("Invalid URL: " + ex.getRequestURL() + " not found", HttpStatus.NOT_FOUND);
    }

    // Handle generic exceptions (fallback for unexpected errors)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object,Object>> handleGenericException(Exception ex) {
        ApiResponse<Object,Object> response = new ApiResponse<Object,Object>(500,ex.getMessage(), ApiResponse.Status.ERROR);
        return new ResponseEntity<ApiResponse<Object,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiResponse<Object,Object>> resourceNotFoundHandler(Exception ex, HttpServletRequest request) {
        String path = request.getRequestURI();
        ApiResponse<Object,Object> response = new ApiResponse<Object,Object>(500,ex.getMessage() + ". Path: " + path, ApiResponse.Status.ERROR);
        return new ResponseEntity<ApiResponse<Object,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}