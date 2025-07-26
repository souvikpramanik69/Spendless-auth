package com.spendless.auth.payload;


import com.spendless.auth.models.Address;
import com.spendless.auth.models.Phone;
import com.spendless.auth.models.Roles;
import jakarta.validation.Valid;
import lombok.*;

import java.util.List;
import jakarta.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserPayload {


    @NotBlank(message = "First name cannot be empty")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String first_name;

    @NotBlank(message = "Last name cannot be empty")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String last_name;

    @NotBlank(message = "Username cannot be empty")
    @Pattern(
            regexp = "^(?=[^@]*@[^@]*$)(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d@]{6,15}$",
            message = "Username must be 6-15 characters long, contain exactly one '@', at least one uppercase letter, and at least one number. No other special characters are allowed."
    )
    private String username;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Email must be a valid email address")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
    @Pattern(
            regexp = "^(?=.{6,15}$)(?!@)(?!.*@\\$)(?=[^@]*@[^@]*$)(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d@]+$",
            message = "Password must be 6-15 characters, include exactly one '@' (not at start or end), one uppercase letter, and one digit."
    )
    private String password;

    @NotEmpty(message = "Phone list cannot be empty")
    @Valid
    private List<Phone> phone;

    @NotEmpty(message = "Address list cannot be empty")
    @Valid
    private List<Address> address;

    private boolean isDeleted;

    private String roles;


    public UserPayload(String email,String password){
        this.email=email;
        this.password = password; 
    }


}
