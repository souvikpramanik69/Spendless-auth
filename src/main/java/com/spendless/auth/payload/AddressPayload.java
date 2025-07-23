package com.spendless.auth.payload;

import com.spendless.auth.models.Users;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
public class AddressPayload {

    private String id;

    @NotBlank(message = "Address line 1 cannot be empty")
    @Size(min = 1, max = 100, message = "Address line 1 must be between 5 and 100 characters")
    private String address1;

    @Size(max = 100, message = "Address line 2 must be less than 100 characters")
    private String address2;

    @NotBlank(message = "Postal code cannot be empty")
    @Pattern(regexp = "\\d{5}(-\\d{4})?|\\d{6}", message = "Postal code must be 5 digits, 5-4 digits, or 6 digits")
    private String postal_code;

    @NotBlank(message = "City cannot be empty")
    @Size(min = 2, max = 50, message = "City must be between 2 and 50 characters")
    private String city;

    @NotBlank(message = "State cannot be empty")
    @Size(min = 2, max = 50, message = "State must be between 2 and 50 characters")
    private String state;

    
    private Users user;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
