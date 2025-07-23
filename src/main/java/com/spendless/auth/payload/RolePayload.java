package com.spendless.auth.payload;

import com.spendless.auth.models.Users;
import jakarta.validation.Payload;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RolePayload {

    String id;

    @NotBlank(message = "Role name can't be empty")
    @Size(min = 2,max = 10,message = "Name should be between 2 and 10 character's")
    String name;


    @Valid
    List<Users> users;






}
