package com.spendless.auth.payload;

import com.spendless.auth.models.Users;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class PhonePayload {

    @NotBlank(message = "Country code cannot be empty")
    @Pattern(regexp = "\\+[1-9][0-9]{0,2}", message = "Country code must start with '+' followed by 1 to 3 digits")
    private String countryCode;

    @NotBlank(message = "Phone number cannot be empty")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be exactly 10 digits and contain only numbers")
    private String number;

    @Pattern(regexp = "\\d{10}|^$", message = "Alternate number must be exactly 10 digits or empty")
    private String alternateNumber;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Users user;

}
