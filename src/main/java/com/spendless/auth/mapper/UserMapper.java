package com.spendless.auth.mapper;

import com.spendless.auth.models.Users;
import com.spendless.auth.dto.UserDto;

public class UserMapper {

    public static UserDto mapToDto(Users payload){
        UserDto dto = new UserDto();
        dto.setId(payload.getId());
          dto.setEmail(payload.getEmail());
          dto.setPhone(payload.getPhone());
          dto.setFirst_name(payload.getFirst_name());
          dto.setLast_name(payload.getLast_name());
          dto.setUsername(payload.getUsername());
          dto.setCreatedAt(payload.getCreatedAt());
          dto.setUpdatedAt(payload.getUpdatedAt());
          dto.setAddress(payload.getAddress());
          dto.setRoles(payload.getRoles());
          dto.setAccessToken(payload.getAccessToken());
          dto.setRefreshToken(payload.getRefreshToken());
          return dto;
    }

}
