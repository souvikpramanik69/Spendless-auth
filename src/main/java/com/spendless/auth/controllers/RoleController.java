package com.spendless.auth.controllers;

import com.spendless.auth.dto.RoleDto;
import com.spendless.auth.mapper.RoleMapper;
import com.spendless.auth.models.Roles;
import com.spendless.auth.payload.RolePayload;
import com.spendless.auth.response.ApiResponse;
import com.spendless.auth.services.Impl.RoleServiceImpl;
import com.spendless.auth.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.management.relation.Role;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/api")
public class RoleController {

  @Autowired
  RoleServiceImpl roleService;

    @GetMapping("/user/{userId}/roles")
    public ResponseEntity<ApiResponse<List<RoleDto>,Object>> getAllRolesByUser(@PathVariable String userId){
        ApiResponse<List<RoleDto>,Object> response;
        try{
            List<Roles> isRoleList = roleService.getAllRolesByUserId(userId);
            if(!isRoleList.isEmpty()) {
                List<RoleDto> roleDtoList = isRoleList.stream().map((item)->{
                    RoleDto dto = RoleMapper.mapToDto(item);
                    return dto;
                }).toList();
                response = new ApiResponse<List<RoleDto>,Object>(200,"Role has been retrieved successfully", ApiResponse.Status.SUCCESS,roleDtoList);
                return new ResponseEntity<ApiResponse<List<RoleDto>,Object>>(response,HttpStatus.OK);
            }
            else {
                response = new ApiResponse<List<RoleDto>,Object>(404,"User doesn't exist", ApiResponse.Status.ERROR);
                return new ResponseEntity<ApiResponse<List<RoleDto>,Object>>(response,HttpStatus.NOT_FOUND);
            }

        }catch (Exception e){
            response = new ApiResponse<List<RoleDto>,Object>(500,e.getMessage(), ApiResponse.Status.ERROR);
            return new ResponseEntity<ApiResponse<List<RoleDto>,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/user/role/{roleId}")
    public ResponseEntity<ApiResponse<Integer,Object>> updateRoleById(@PathVariable String roleId, @RequestBody RolePayload payload){
        ApiResponse<Integer,Object> response;
        try{
                Integer isRoleExist = roleService.updateRoleById(roleId,payload);
                if(isRoleExist == 1){
                    response = new ApiResponse<Integer,Object>(200,"Role has been updated successfully", ApiResponse.Status.SUCCESS);
                    return new ResponseEntity<ApiResponse<Integer,Object>>(response,HttpStatus.OK);
                }else if(isRoleExist == 0) {
                    response = new ApiResponse<Integer,Object>(403,"You don't have permission to modify role", ApiResponse.Status.ERROR);
                    return new ResponseEntity<ApiResponse<Integer,Object>>(response,HttpStatus.FORBIDDEN);
                }else {
                    response = new ApiResponse<Integer,Object>(404,"User doesn't exist", ApiResponse.Status.ERROR);
                    return new ResponseEntity<ApiResponse<Integer,Object>>(response,HttpStatus.NOT_FOUND);
                }
        }catch (Exception e){
            response = new ApiResponse<Integer,Object>(500,e.getMessage(), ApiResponse.Status.ERROR);
            return new ResponseEntity<ApiResponse<Integer,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
        }
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


