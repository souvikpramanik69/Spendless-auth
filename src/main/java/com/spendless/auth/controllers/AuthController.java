package com.spendless.auth.controllers;


import com.spendless.auth.models.Users;
import com.spendless.auth.dto.UserDto;
import com.spendless.auth.mapper.UserMapper;
import com.spendless.auth.payload.AuthPayload;
import com.spendless.auth.payload.UserPayload;
import com.spendless.auth.response.ApiResponse;
import com.spendless.auth.services.Impl.UserServiceImpl;
import com.spendless.auth.services.JWTService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private UserServiceImpl authService;

    @Autowired
    JWTService jwtUtil;

    @Autowired
    AuthenticationManager authManager;

    //    @Secured("USER")
    @PostMapping("/auth/register")
    public ResponseEntity<ApiResponse<UserDto, Object>> register(@Valid @RequestBody UserPayload payload) {
        ApiResponse<UserDto, Object> response;
        try {

            Users user = authService.register(payload);
            if (user != null) {
                UserDto dtoData = UserMapper.mapToDto(user);
                response = new ApiResponse<>(201, "User register successfully", ApiResponse.Status.SUCCESS, dtoData);
                return new ResponseEntity<ApiResponse<UserDto, Object>>(response, HttpStatus.CREATED);
            } else {
                response = new ApiResponse<UserDto, Object>(409, "User already exist", ApiResponse.Status.ERROR);
                return new ResponseEntity<ApiResponse<UserDto, Object>>(response, HttpStatus.CONFLICT);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            response = new ApiResponse<UserDto, Object>(500, e.getMessage(), ApiResponse.Status.ERROR);
            return new ResponseEntity<ApiResponse<UserDto, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/auth/login")
    public ResponseEntity<ApiResponse<UserDto,Object>> login(@RequestBody @Valid AuthPayload payload) {
        try {
//            logger.info("Attempting login for user: {}", payload.getUsername());

            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(payload.getUsername(), payload.getPassword())
            );

            if (authentication.isAuthenticated() && authentication.getPrincipal() != null) {
                if (authentication.getPrincipal() instanceof Users) {
                    String accessToken  = jwtUtil.generateAccessToken(payload.getUsername());
                    String refreshToken  = jwtUtil.generateRefreshToken(payload.getUsername());
                    Users user = (Users) authentication.getPrincipal();
                    user.setRefreshToken(refreshToken);
                    user.setAccessToken(accessToken);
//                    logger.info("User {} authenticated successfully", payload.getUsername());
                    UserDto userDto = UserMapper.mapToDto(user); // Map Users to UserDto
                    return ResponseEntity.ok(
                            new ApiResponse<>(
                                    HttpStatus.OK.value(),
                                    "Login successful",
                                    ApiResponse.Status.SUCCESS,
                                    userDto
                            )
                    );
                } else {
//                    logger.error("Principal is not of type Users: {}", authentication.getPrincipal());
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                            new ApiResponse<>(
                                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                    "Unexpected principal type",
                                    ApiResponse.Status.ERROR,
                                    null
                            )
                    );
                }
            } else {
//                logger.warn("Authentication failed or principal is null for user: {}", payload.getUsername());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                        new ApiResponse<>(
                                HttpStatus.UNAUTHORIZED.value(),
                                "User doesn't exist",
                                ApiResponse.Status.ERROR,
                                null
                        )
                );
            }
        } catch (UsernameNotFoundException e) {
//            logger.warn("User not found: {}", payload.getUsername());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ApiResponse<>(
                            HttpStatus.NOT_FOUND.value(),
                            "User doesn't exist",
                            ApiResponse.Status.ERROR
                    )
            );
        } catch (AuthenticationException e) {
//            logger.error("Authentication failed for user: {}", payload.getUsername(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ApiResponse<>(
                            HttpStatus.NOT_FOUND.value(),
                            "User doesn't exist",
                            ApiResponse.Status.ERROR
                    )
            );
        } catch (Exception e) {
//            logger.error("Unexpected error during login for user: {}", payload.getUsername(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiResponse<>(
                            HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "An unexpected error occurred",
                            ApiResponse.Status.ERROR,
                            null
                    )
            );
        }
    }


    //----------------------------Form Validation Exception---------------------------------------------
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errorList = new ArrayList<String>();
        for (org.springframework.validation.FieldError
                error : ex.getBindingResult().getFieldErrors()) {
            errorList.add(error.getDefaultMessage());
        }
        ApiResponse<Object, Object> response = new ApiResponse<>(400, errorList, ApiResponse.Status.ERROR, null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiResponse<Object, String>> handleUsernameNotFound(UsernameNotFoundException ex) {
        ApiResponse<Object, String> response = new ApiResponse<>(
                401,
                ex.getMessage(), // Or use a custom message like "Invalid username or password"
                ApiResponse.Status.ERROR,
                null
        );

        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);

    }
}

