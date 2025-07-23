package com.spendless.auth.repositories;

import com.spendless.auth.models.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Roles,String> {

    @Query("SELECT r FROM Roles r JOIN r.users u WHERE u.id = :userId AND r.id = :roleId")
    Roles findByIdAndUser(@Param("userId") String userId, @Param("roleId") String roleId);

    Optional<Roles> findByName (@Param("name") String name);

}
