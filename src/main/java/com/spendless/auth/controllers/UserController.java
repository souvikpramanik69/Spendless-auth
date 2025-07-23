package com.spendless.auth.controllers;

import com.spendless.auth.models.Users;
import com.spendless.auth.dto.UserDto;
import com.spendless.auth.mapper.UserMapper;
import com.spendless.auth.payload.UserPayload;
import com.spendless.auth.repositories.UserRepository;
import com.spendless.auth.response.ApiResponse;
import com.spendless.auth.response.Pagination;
import com.spendless.auth.services.Impl.UserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")

public class UserController {
    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserRepository userRepo;

    @PutMapping("/user/{id}")
    public ResponseEntity<ApiResponse<UserDto,Object>> updateUser(@RequestBody @Valid UserPayload payload, @PathVariable("id") String id){
        System.out.println("getting id "  +id);
        ApiResponse<UserDto,Object> response;
        Users isUserExist = userService.updateUser(payload,id);
        try{
            if(isUserExist != null){
                UserDto dtoData = UserMapper.mapToDto(isUserExist);
                response = new ApiResponse<UserDto,Object>(200,"User updated successfully",ApiResponse.Status.SUCCESS,dtoData);
                return new ResponseEntity<ApiResponse<UserDto,Object>>(response, HttpStatus.OK);
            }
            else {
                response = new ApiResponse<UserDto,Object>(404,"Invalid username, email, or password",ApiResponse.Status.ERROR);
                return new ResponseEntity<ApiResponse<UserDto,Object>>(response,HttpStatus.NOT_FOUND);
            }
        }catch (Exception e){
            response = new ApiResponse<UserDto,Object>(500,e.getMessage(), ApiResponse.Status.ERROR);
            return new ResponseEntity<ApiResponse<UserDto,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/user")
    public ResponseEntity<ApiResponse<Integer,Object>> deleteUser( @RequestParam("id") String id){
       ApiResponse<Integer,Object> response;
       try{
            Integer result = userService.deleteUser(id);
            if(result == 1){
                response = new ApiResponse<Integer,Object>(200,"User Deleted Successfully", ApiResponse.Status.SUCCESS);
                return new ResponseEntity<ApiResponse<Integer,Object>>(response,HttpStatus.OK);
            }
            else {
                response = new ApiResponse<Integer,Object>(404,"User doesn't exist", ApiResponse.Status.ERROR);
                return new ResponseEntity<ApiResponse<Integer,Object>>(response,HttpStatus.NOT_FOUND);
            }
       }catch (Exception e){
           response = new ApiResponse<Integer,Object>(500,e.getMessage(), ApiResponse.Status.ERROR);
           return new ResponseEntity<ApiResponse<Integer,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
       }
    }


    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<UserDto>,Object>> allUsers(
            @RequestParam("isdeleted") boolean isDeleted,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "limit", defaultValue = "10") int limit,
            @RequestParam(name = "name",defaultValue = "") String name)
    {

        ApiResponse<List<UserDto>,Object> response;
        Pagination pageData = new Pagination();
            Pageable pageable = PageRequest.of(page - 1, limit);
        Page<Users> userPage;
        try {
             if(!name.isEmpty()){
                userPage = userRepo.findByIsDeletedAndNameContaining(isDeleted, name, pageable);
                 pageData.setTotalItems(userPage.getTotalElements());

             }
             else {
                 userPage = userRepo.findByIsDeleted(isDeleted, pageable);
                 pageData.setTotalItems(userPage.getTotalElements());
             }

            List<UserDto> dtoData = userPage.getContent().stream()
                    .map(UserMapper::mapToDto)
                    .toList();

            int totalPages = !dtoData.isEmpty() ? (int)userPage.getTotalElements()/limit : 0;
             pageData.setTotalPages(totalPages);
             pageData.setCurrentPage(page);
             pageData.setLimit(limit);
            if (!dtoData.isEmpty()) {
                response = new ApiResponse<List<UserDto>,Object>(200, "Users retrieved successfully", ApiResponse.Status.SUCCESS, dtoData,pageData);
                return new ResponseEntity<ApiResponse<List<UserDto>,Object>>(response, HttpStatus.OK); // Corrected to HttpStatus.OK
            } else {
                response = new ApiResponse<List<UserDto>,Object>(404, "No users available", ApiResponse.Status.ERROR);
                return new ResponseEntity<ApiResponse<List<UserDto>,Object>>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            response = new ApiResponse<>(500, e.getMessage(), ApiResponse.Status.ERROR);
            return new ResponseEntity<ApiResponse<List<UserDto>,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //----------------------------Form Validation Exception---------------------------------------------
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object,Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errorList = new ArrayList<String>();
        for (org.springframework.validation.FieldError
                error : ex.getBindingResult().getFieldErrors()) {
            errorList.add(error.getDefaultMessage());
        }
        ApiResponse<Object,Object> response = new ApiResponse<>(400, errorList, ApiResponse.Status.ERROR, null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiResponse<Object,Object>> invalidUser(MethodArgumentNotValidException ex) {
        List<String> errorList = new ArrayList<String>();
        for (org.springframework.validation.FieldError
                error : ex.getBindingResult().getFieldErrors()) {
            errorList.add(error.getDefaultMessage());
        }
        ApiResponse<Object,Object> response = new ApiResponse<>(401, errorList, ApiResponse.Status.ERROR, null);
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }







}
