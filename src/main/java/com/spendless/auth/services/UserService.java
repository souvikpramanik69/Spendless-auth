package com.spendless.auth.services;



import com.spendless.auth.models.Users;
import com.spendless.auth.payload.UserPayload;

import java.util.List;

public interface UserService {

    public Users register(UserPayload payload);
    public Users updateUser(UserPayload payload,String id);
    public Integer deleteUser(String id);
    public List<Users> getAllUsers(boolean isDeleted);
    


}
