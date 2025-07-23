package com.spendless.auth.services.Impl;

import com.spendless.auth.models.Roles;
import com.spendless.auth.models.Users;
import com.spendless.auth.payload.RolePayload;
import com.spendless.auth.repositories.RoleRepository;
import com.spendless.auth.repositories.UserRepository;
import com.spendless.auth.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;

    public List<Roles> getAllRolesByUserId(String userId){
        Optional<Users> isUserExist = userRepository.findByIdAndIsDeleted(userId,false);
        if(isUserExist.isPresent()){
            List<Roles> userRoles = isUserExist.get().getRoles();
            System.out.println("ALl Roles "  + userRoles );
            return userRoles;
        }
        else return null;
    }

    public Integer updateRoleById(String roleId,RolePayload payload){
       Optional<Roles> isUserExist = roleRepository.findById(roleId);
       if(isUserExist.isPresent() && isUserExist.get().getName().equals("ROLE_ADMIN") ){
           isUserExist.get().setName(payload.getName());
           roleRepository.save(isUserExist.get());
           return 1;
       }
       else if(isUserExist.isPresent() && !isUserExist.get().getName().equals("ROLE_ADMIN")){
           return 0;
       }
       else {
           return 2;
       }

    }



}
