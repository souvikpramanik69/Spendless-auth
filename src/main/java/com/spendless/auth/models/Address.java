package com.spendless.auth.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Address {

   @Id
   @Column(name = "id", updatable = false, nullable = false)// Inbuilt UUID for Java
   private String id =  UUID.randomUUID().toString();
   @NotBlank(message = "Address 1 can't be empty")
   @Size(min=5,max=100,message = "Address must be between 5 and 100 character's")
   private String address1;

   @Size(max=100,message = "Address 2  must not exceed 15 character's")
   private String address2;

   @NotBlank(message = "Postal Code must be empty")
   @Size(min = 6,max = 6,message = "Postal Code must be 6 digit")
   private String postal_code;
   @NotBlank(message = "City can't be empty")
   private String city;
   @NotBlank(message = "State can't be empty")
   private String state;

   @ManyToOne
   @JsonBackReference
   @Valid
   private Users user;


   @CreationTimestamp
   @Column(name = "created_at",nullable = false,updatable = false)
   private LocalDateTime createdAt=LocalDateTime.now();

   @UpdateTimestamp
   @Column(name = "updated_at",nullable = false)
   private LocalDateTime updatedAt;



}
