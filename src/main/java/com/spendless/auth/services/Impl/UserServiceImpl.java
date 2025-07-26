package com.spendless.auth.services.Impl;


import com.spendless.auth.models.Address;
import com.spendless.auth.models.Phone;
import com.spendless.auth.models.Roles;
import com.spendless.auth.models.Users;
import com.spendless.auth.payload.UserPayload;
import com.spendless.auth.repositories.RoleRepository;
import com.spendless.auth.repositories.UserRepository;
import com.spendless.auth.services.UserService;
import com.spendless.auth.services.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repo;


    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JWTService jwtUtil;
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public Users register(UserPayload payload){

        if (payload.getEmail() == null || !payload.getEmail().matches("^[A-Za-z0-9._%+-]+@gmail\\.com$")) {
            throw new IllegalArgumentException("Invalid Gmail address");
        }
        else {
            String accessToken  = jwtUtil.generateAccessToken(payload.getUsername());
            String refreshToken  = jwtUtil.generateRefreshToken(payload.getUsername());
            Optional<Users> isUserExist = repo.findByUsernameOrEmailAndIsDeleted(payload.getUsername(), payload.getEmail(), payload.isDeleted());
            if (isUserExist.isPresent()) return null;
            Users newUser = new Users();
            newUser.setId(UUID.randomUUID().toString());
            newUser.setUsername(payload.getUsername());
            newUser.setEmail(payload.getEmail());
            newUser.setPassword(encoder.encode(payload.getPassword()));
            newUser.setFirst_name(payload.getFirst_name());
            newUser.setLast_name(payload.getLast_name());
            newUser.setAccessToken(accessToken);
            newUser.setRefreshToken(refreshToken);
            String roles = "ROLE_"+payload.getRoles();
            Roles role = roleRepository.findByName(roles)
                    .orElseThrow(() -> new RuntimeException("Role not found"));
            List<Roles> roleList = new ArrayList<Roles>();
            roleList.add(role);
            newUser.setRoles(roleList);


            List<Phone> phones = payload.getPhone();
               phones.get(0).setUpdatedAt(LocalDateTime.now());
               phones.get(0).setId(UUID.randomUUID().toString());
               newUser.setPhone(phones);

            // Handle addresses
            List<Address> addresses = payload.getAddress();
            addresses.get(0).setUpdatedAt(LocalDateTime.now());
            addresses.get(0).setId(UUID.randomUUID().toString());
            newUser.setAddress(addresses);



            return repo.save(newUser);
        }

    }

    public Users updateUser(UserPayload payload,String id){
        Optional<Users> isUserExist = repo.findByUsernameOrEmailAndIsDeleted(payload.getUsername(), payload.getEmail(), payload.isDeleted());
        if(isUserExist.isPresent() &&  (Objects.equals(isUserExist.get().getPassword(), payload.getPassword())) &&
         (Objects.equals(isUserExist.get().getUsername(), payload.getUsername())) &&  (Objects.equals(isUserExist.get().getEmail(), payload.getEmail())) ){
            isUserExist.get().setUsername(payload.getUsername());
            isUserExist.get().setFirst_name(payload.getFirst_name());
            isUserExist.get().setLast_name(payload.getLast_name());
            isUserExist.get().setUsername(payload.getUsername());
            isUserExist.get().setPassword(payload.getPassword());
            isUserExist.get().setPhone(payload.getPhone());
            isUserExist.get().setUsername(payload.getUsername());
            isUserExist.get().setEmail(payload.getEmail());
            isUserExist.get().setAddress(payload.getAddress());
            return repo.save(isUserExist.get());
        }
        return null;
    }

    public Integer deleteUser(String id){
        Optional<Users> isUserExist = repo.findByIdAndIsDeleted(id,false);
        if(isUserExist.isEmpty()) return 0;
        isUserExist.get().setDeleted(true);
        repo.save(isUserExist.get());
        return 1;
    }

    public List<Users> getAllUsers(boolean isDeleted){

        List<Users> allUsers = repo.getAllUsers(isDeleted);
        return allUsers;
    }

    
    








}
