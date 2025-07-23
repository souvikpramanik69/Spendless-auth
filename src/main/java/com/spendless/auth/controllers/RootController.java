package com.spendless.auth.controllers;


import com.spendless.auth.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RootController {


    @GetMapping("/api/auth")
    public ResponseEntity<ApiResponse<String,Object>> home(){
        ApiResponse<String,Object> response = new ApiResponse<String,Object>(200,"Welcome to SpendLess Auth Service", ApiResponse.Status.SUCCESS);
        return new ResponseEntity<ApiResponse<String,Object>>(response, HttpStatus.NOT_FOUND);
    }

    //----------------------------Form Validation Exception---------------------------------------------
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object,Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errorList = new ArrayList<>();
        for (org.springframework.validation.FieldError
                error : ex.getBindingResult().getFieldErrors()) {
            errorList.add(error.getDefaultMessage());
        }
        ApiResponse<Object,Object> response = new ApiResponse<>(400, errorList, ApiResponse.Status.ERROR, null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


}
