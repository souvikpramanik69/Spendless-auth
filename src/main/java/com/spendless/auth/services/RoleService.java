package com.spendless.auth.services;

import com.spendless.auth.models.Roles;
import com.spendless.auth.payload.RolePayload;

import javax.naming.NoPermissionException;
import java.util.List;

public interface RoleService {

    public List<Roles> getAllRolesByUserId(String userId);
    public Integer updateRoleById(String roleId, RolePayload payload);
    public Roles addRole(RolePayload payload) throws NoPermissionException;

}
