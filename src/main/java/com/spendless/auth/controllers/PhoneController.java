package com.spendless.auth.controllers;


import com.spendless.auth.models.Phone;
import com.spendless.auth.dto.PhoneDto;
import com.spendless.auth.mapper.PhoneMapper;
import com.spendless.auth.payload.PhonePayload;
import com.spendless.auth.response.ApiResponse;
import com.spendless.auth.response.Pagination;
import com.spendless.auth.services.PhoneService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/user")

public class PhoneController {

    @Autowired
   private PhoneService phoneService;


         @GetMapping("/{userId}/phones")
         public ResponseEntity<ApiResponse<List<PhoneDto>,Object>> getAllPhoneByUserId(
                 @PathVariable("userId") String userId,
                 @RequestParam(name = "page", defaultValue = "1") int page, @RequestParam(name = "limit", defaultValue = "10") int limit){
    ApiResponse<List<PhoneDto>,Object> response;
    Pageable pageable = PageRequest.of(page - 1, limit);
  try{
       Page<Phone> phoneData = phoneService.getAllPhonesByUserId(userId,pageable);
       if( phoneData!= null ){
           List<PhoneDto> dtoData = phoneData.stream().map((item->{
               PhoneDto dto = PhoneMapper.mapToDto(item);
               return dto;
           })).toList();
           Pagination pageData = new Pagination();
           int totalPages = !dtoData.isEmpty() ? (int)phoneData.getTotalElements()/limit : 0;
           int totalItems =  !dtoData.isEmpty() ?  (int)phoneData.getTotalElements() : limit;
           pageData.setTotalPages(totalPages);
           pageData.setCurrentPage(page);
           pageData.setTotalItems(totalItems);
           pageData.setLimit(limit);

           response = new ApiResponse<List<PhoneDto>,Object>(200,"All Phone has been retrieved", ApiResponse.Status.SUCCESS,dtoData,pageData);
           return new ResponseEntity<>(response, HttpStatus.OK);
       }
       else {
           response = new ApiResponse<List<PhoneDto>,Object>(404,"User doesn't exist", ApiResponse.Status.ERROR);
           return new ResponseEntity<ApiResponse<List<PhoneDto>,Object>>(response,HttpStatus.NOT_FOUND);
       }
  }catch (Exception e){
         response = new ApiResponse<List<PhoneDto>,Object>(500,e.getMessage(), ApiResponse.Status.ERROR);
         return new ResponseEntity<ApiResponse<List<PhoneDto>,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}


         @PostMapping("/{userId}/phone")
         public ResponseEntity<ApiResponse<PhoneDto,Object>> addPhone(@Valid @RequestBody PhonePayload phone, @PathVariable String userId){
 ApiResponse<PhoneDto,Object> response;
 try{
       Phone addPhone = phoneService.addPhone(phone,userId);
     System.out.println("add Phone Data " + addPhone);
       if(addPhone != null) {
           response = new ApiResponse<PhoneDto,Object>(201,"Phone is added successfully", ApiResponse.Status.SUCCESS,PhoneMapper.mapToDto(addPhone));
           return new ResponseEntity<ApiResponse<PhoneDto,Object>>(response,HttpStatus.CREATED);
       }
       else {
           response = new ApiResponse<PhoneDto,Object>(404,"User doesn't exist", ApiResponse.Status.ERROR);
           return new ResponseEntity<ApiResponse<PhoneDto,Object>>(response,HttpStatus.NOT_FOUND);
       }
 }catch(Exception e){
   response = new ApiResponse<PhoneDto,Object>(500,e.getMessage(), ApiResponse.Status.ERROR);
   return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
 }
}

         @GetMapping("/{userId}/phones/{phoneId}")
         public ResponseEntity<ApiResponse<PhoneDto,Object>> getPhoneByUserId(@PathVariable("userId")String userId,@PathVariable("phoneId")String phoneId){
    ApiResponse<PhoneDto,Object> response;
    try{
        Phone isExist = phoneService.getPhoneIdAndByUserId(phoneId,userId);
        if(isExist != null) {
            response = new ApiResponse<PhoneDto,Object>(200,"Phone has been retrieved", ApiResponse.Status.SUCCESS,PhoneMapper.mapToDto(isExist));
            return new ResponseEntity<ApiResponse<PhoneDto,Object>>(response,HttpStatus.OK);
        }
        else {
            response = new ApiResponse<PhoneDto,Object>(404,"User or Phone doesn't exist", ApiResponse.Status.ERROR);
            return new ResponseEntity<ApiResponse<PhoneDto,Object>>(response,HttpStatus.NOT_FOUND);
        }
    }
    catch (Exception e){
        response = new ApiResponse<PhoneDto,Object>(500,e.getMessage(), ApiResponse.Status.ERROR);
        return new ResponseEntity<ApiResponse<PhoneDto,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
    }
    }

        @PutMapping("/{userId}/phone/{phoneId}")
        public ResponseEntity<ApiResponse<PhoneDto,Object>> updatePhoneByIdAndUserId(@PathVariable("userId")String userId,@PathVariable("phoneId")String phoneId, @Valid @RequestBody PhonePayload dto){
    ApiResponse<PhoneDto,Object> response;
    try{
        Phone isExist = phoneService.updatePhoneByIdAndUserId(phoneId,userId,dto);
        if(isExist != null) {
            response = new ApiResponse<PhoneDto,Object>(200,"Phone has been updated", ApiResponse.Status.SUCCESS,PhoneMapper.mapToDto(isExist));
            return new ResponseEntity<ApiResponse<PhoneDto,Object>>(response,HttpStatus.OK);
        }
        else {
            response = new ApiResponse<PhoneDto,Object>(404,"User or Phone doesn't exist", ApiResponse.Status.ERROR);
            return new ResponseEntity<ApiResponse<PhoneDto,Object>>(response,HttpStatus.NOT_FOUND);
        }
    }
    catch (Exception e){
        response = new ApiResponse<PhoneDto,Object>(500,e.getMessage(), ApiResponse.Status.ERROR);
        return new ResponseEntity<ApiResponse<PhoneDto,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
    }
    }


        @DeleteMapping("/{userId}/phone/{phoneId}")
        public ResponseEntity<ApiResponse<Integer,Object>> deleteUserById(@PathVariable("phoneId") String phoneId,@PathVariable("userId") String userId){
         ApiResponse<Integer,Object> response;
          try{
             Integer result = phoneService.deletePhoneById(phoneId,userId);
             if(result == 1){
               response = new ApiResponse<Integer,Object>(200,"Phone remove successfully", ApiResponse.Status.SUCCESS);
               return new ResponseEntity<ApiResponse<Integer,Object>>(response,HttpStatus.OK);

             }
             else {
                 response = new ApiResponse<Integer,Object>(404,"User doesn't exist", ApiResponse.Status.ERROR);
                 return new ResponseEntity<ApiResponse<Integer,Object>>(response,HttpStatus.NOT_FOUND);
             }
          }catch (Exception e){
              response = new ApiResponse<Integer,Object>(500,e.getMessage(), ApiResponse.Status.ERROR);
              return new ResponseEntity<ApiResponse<Integer,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
          }
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
