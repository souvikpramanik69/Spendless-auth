package com.spendless.auth.repositories;

import com.spendless.auth.models.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Roles,String> {

    @Query("SELECT r FROM Roles r JOIN r.users u WHERE u.id = :userId AND r.id = :roleId")
    Roles findByIdAndUser(@Param("userId") String userId, @Param("roleId") String roleId);

    Optional<Roles> findByName (@Param("name") String name);

    @Query(value = """
    SELECT r.* FROM roles r
    JOIN user_roles ur ON ur.role_id = r.id
    WHERE ur.user_id = :userId AND r.name = :name
""", nativeQuery = true)
    Roles findRoleByUserIdAndName(@Param("userId") String userId, @Param("name") String name);

}
