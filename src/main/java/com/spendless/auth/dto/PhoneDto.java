package com.spendless.auth.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spendless.auth.models.Users;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PhoneDto {

    private String id;
    private String countryCode;
    private String number;
    private String alternateNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @JsonIgnore
    private Users user;

}
