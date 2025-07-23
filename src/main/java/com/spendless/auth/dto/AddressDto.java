package com.spendless.auth.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spendless.auth.models.Users;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
public class AddressDto {

    private String id;
    private String address1;
    private String address2;
    private String postal_code;
    private String city;
    private String state;
    @JsonIgnore
    private Users user;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
