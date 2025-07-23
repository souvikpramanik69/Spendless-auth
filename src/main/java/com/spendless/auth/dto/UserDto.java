package com.spendless.auth.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import com.spendless.auth.models.Address;
import com.spendless.auth.models.Phone;
import com.spendless.auth.models.Roles;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {

    private String id;
    private String first_name;
    private String last_name;
    private String username;
    private String email;
    private String password;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    private List<Phone> phone;
    private List<Address> address;
    private List<Roles> roles;
    private String accessToken;
    private String refreshToken;


}
