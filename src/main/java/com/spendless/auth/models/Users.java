package com.spendless.auth.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
@Component
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Users implements UserDetails {


    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @NotBlank(message = "first name cannot be empty")
    private String first_name;

    @NotBlank(message = "last name cannot be empty")
    private String last_name;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Email should be valid")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@gmail\\.com$", message = "Email must be a valid Gmail address")
    private String email;

//    @Length(min = 6,max = 55,message = "Password length must be 6 to 15 character's")
//    @Pattern(
//            regexp = "^(?=.{6,15}$)(?!@)(?!.*@\\$)(?=[^@]*@[^@]*$)(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d@]+$",
//            message = "Password must be 6-15 characters, include exactly one '@' (not at start or end), one uppercase letter, and one digit."
//    )
    private String password;

//    @ElementCollection
    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles", // Name of the join table
            joinColumns = @JoinColumn(name = "user_id"), // Foreign key for the owning entity
            inverseJoinColumns = @JoinColumn(name = "role_id") // Foreign key for the referenced entity
    )
    private List<Roles> roles;

    @Size(min = 6,max = 15,message = "Username must be between 6 to 15 character's")
    @NotBlank(message = "username can't be empty")
    private String username;

    private boolean isDeleted = false;

    @CreationTimestamp
    @Column(name = "created_at",nullable = false,updatable = false)
    private LocalDateTime createdAt=LocalDateTime.now();

    @UpdateTimestamp
    @Column(name = "updated_at",nullable = false)
    @JoinColumn(name = "user_id")
    private LocalDateTime updatedAt;

    @OneToMany(cascade = CascadeType.ALL)
    @JsonManagedReference
    @JoinColumn(name = "user_id")
    private List<Phone> phone;

    @Column(name = "access_token")
    private String accessToken;
    @Column(name = "refresh_token")
    private String refreshToken;


    @OneToMany(cascade = CascadeType.ALL)
    @JsonManagedReference
    @JoinColumn(name = "user_id")
    private List<Address> address;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
