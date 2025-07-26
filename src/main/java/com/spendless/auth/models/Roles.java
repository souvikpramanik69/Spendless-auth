package com.spendless.auth.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Roles {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @Size(min = 2,max = 40,message = "Name must be between 2 and 10")
    private String name;

    @ManyToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
    @JsonIgnore
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Users> users;

}
