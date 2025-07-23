package com.spendless.auth.mapper;

import com.spendless.auth.dto.RoleDto;
import com.spendless.auth.models.Roles;
import com.spendless.auth.payload.RolePayload;
import lombok.Data;

@Data
public class RoleMapper {

    public static RoleDto mapToDto(Roles payload){
      RoleDto dto = new RoleDto();
      dto.setId(payload.getId());
      dto.setName(payload.getName());
      return dto;
    }
}
