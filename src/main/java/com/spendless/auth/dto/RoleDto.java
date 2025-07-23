package com.spendless.auth.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.spendless.auth.models.Users;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleDto {

    private String id;

    private String name;

    private List<Users> users;
}
