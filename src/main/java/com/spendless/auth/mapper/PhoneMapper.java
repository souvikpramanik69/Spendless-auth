package com.spendless.auth.mapper;


import com.spendless.auth.models.Phone;
import com.spendless.auth.dto.PhoneDto;

public class PhoneMapper {

    public static PhoneDto mapToDto(Phone payload){
       PhoneDto dto = new PhoneDto();
       dto.setId(payload.getId());
       dto.setNumber(payload.getNumber());
       dto.setUser(payload.getUser());
       dto.setCountryCode(payload.getCountryCode());
       dto.setCreatedAt(payload.getCreatedAt());
       dto.setUpdatedAt(payload.getUpdatedAt());
       dto.setAlternateNumber(payload.getAlternateNumber());
        return dto;
    }

}
