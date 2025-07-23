package com.spendless.auth.mapper;


import com.spendless.auth.models.Address;
import com.spendless.auth.dto.AddressDto;

public class AddressMapper {


    public static AddressDto mapToDto(Address payload){
         AddressDto newDto = new AddressDto();
         newDto.setId(payload.getId());
         newDto.setAddress1(payload.getAddress1());
         newDto.setAddress2(payload.getAddress2());
         newDto.setCity(payload.getCity());
         newDto.setState(payload.getState());
         newDto.setPostal_code(payload.getPostal_code());
         newDto.setUser(payload.getUser());
         newDto.setUpdatedAt(payload.getUpdatedAt());
         newDto.setCreatedAt(payload.getCreatedAt());
         return newDto;
    }

}
