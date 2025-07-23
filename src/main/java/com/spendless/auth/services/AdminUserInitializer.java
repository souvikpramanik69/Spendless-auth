package com.spendless.auth.services;

import com.spendless.auth.models.Address;
import com.spendless.auth.models.Phone;
import com.spendless.auth.models.Roles;
import com.spendless.auth.models.Users;
import com.spendless.auth.repositories.RoleRepository;
import com.spendless.auth.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Role;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootApplication
public class AdminUserInitializer {

    @Autowired
    UserRepository userRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private RoleRepository roleRepository;

    @Bean
    public CommandLineRunner addAdmin() {
return args -> {

    Optional<Users> isUserExist = userRepository.findByUsername("admin@123");
    if (isUserExist.isEmpty()) {
        Address adminAddress = new Address();
        adminAddress.setId(UUID.randomUUID().toString());
        adminAddress.setAddress1("Kolkata");
        adminAddress.setAddress2("Kestopur");
        adminAddress.setCity("Newtown");
        adminAddress.setPostal_code("700102");
        adminAddress.setState("West Bengal");


        Phone adminPhone = new Phone();
        adminPhone.setCountryCode("+91");
        adminPhone.setId(UUID.randomUUID().toString());
        adminPhone.setAlternateNumber("9547263526");
        adminPhone.setNumber("8617584965");

        List<Phone> adminPhoneList = new ArrayList<Phone>();
        adminPhoneList.add(adminPhone);


        Users adminUser = new Users();
        adminUser.setUsername("admin@123");
        adminUser.setFirst_name("Admin");
        adminUser.setLast_name("User");
        adminUser.setId(UUID.randomUUID().toString());
        adminUser.setEmail("spendlessadmin@gmail.com");

        String rawPassword = "testdev";
        String encodedPassword = passwordEncoder().encode(rawPassword);
        adminUser.setPassword(encodedPassword);

         Roles newRole = new Roles();
         newRole.setId(UUID.randomUUID().toString());
         newRole.setName("ROLE_ADMIN");

         List<Roles> roleList = new ArrayList<Roles>();


        List<Address> addressList = new ArrayList<Address>();
        addressList.add(adminAddress);


        adminUser.setRoles(roleList);
        adminUser.setAddress(addressList);
        adminUser.setPhone(adminPhoneList);
        userRepository.save(adminUser);
         roleRepository.save(newRole);

        System.out.println("Default admin user created");
    };
};

    }



}
