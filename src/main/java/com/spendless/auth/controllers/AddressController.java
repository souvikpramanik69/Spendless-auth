package com.spendless.auth.controllers;



import com.spendless.auth.models.Address;
import com.spendless.auth.dto.AddressDto;
import com.spendless.auth.mapper.AddressMapper;
import com.spendless.auth.payload.AddressPayload;
import com.spendless.auth.response.ApiResponse;
import com.spendless.auth.services.Impl.AddressServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class AddressController {

    @Autowired
    private AddressServiceImpl addressService;

    @GetMapping("/{userId}/addresses")
    public ResponseEntity<ApiResponse<List<AddressDto>,Object>> getAllAddressByUserId(@PathVariable("userId")String userId,
                                                                                      @RequestParam(name = "page",defaultValue = "1")int page,
                                                                                      @RequestParam(name = "limit",defaultValue = "10")int limit){
        ApiResponse<List<AddressDto>,Object> response;
        try{
           List<AddressDto> data = addressService.getAllAddressByUserId(userId,page,limit);
           if(data != null){
               response = new ApiResponse<List<AddressDto>,Object>(200,"All address are retrieved", ApiResponse.Status.SUCCESS,data);
               return new ResponseEntity<ApiResponse<List<AddressDto>,Object>>(response, HttpStatus.OK);
           }
           else {
               response = new ApiResponse<List<AddressDto>,Object>(404,"User doesn't exist", ApiResponse.Status.ERROR);
               return new ResponseEntity<ApiResponse<List<AddressDto>,Object>>(response, HttpStatus.NOT_FOUND);
           }

        }catch (Exception e){
            response = new ApiResponse<List<AddressDto>,Object>(500,e.getMessage(), ApiResponse.Status.SUCCESS);
            return new ResponseEntity<ApiResponse<List<AddressDto>,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{userId}/address")
    public ResponseEntity<ApiResponse<AddressDto,Object>> addAddress(@RequestBody @Valid AddressPayload payload, @PathVariable("userId")String userId){
     ApiResponse<AddressDto,Object> response;
     try{
       Address isUserExist = addressService.addAddressByUserId(payload,userId);
       if(isUserExist != null){
           response = new ApiResponse<AddressDto,Object>(200,"Address added successfully", ApiResponse.Status.SUCCESS, AddressMapper.mapToDto(isUserExist));
           return new ResponseEntity<ApiResponse<AddressDto,Object>>(response,HttpStatus.OK);
       }
       else {
           response = new ApiResponse<AddressDto,Object>(404,"User doesn't exist", ApiResponse.Status.ERROR);
           return new ResponseEntity<ApiResponse<AddressDto,Object>>(response,HttpStatus.NOT_FOUND);
       }
     }catch (Exception e){
         response = new ApiResponse<AddressDto,Object>(500, e.toString(), ApiResponse.Status.ERROR);
         return new ResponseEntity<ApiResponse<AddressDto,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
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


//    public ResponseEntity<ApiResponse<Object,Object>>

}


