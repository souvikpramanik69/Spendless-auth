package com.spendless.auth.repositories;


import com.spendless.auth.models.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users,String> {

    Optional<Users> findByUsername(String username);

    @Query(
            value = "SELECT * FROM Users WHERE (username = :username OR email = :email) AND is_deleted = :isDeleted",
            nativeQuery = true
    )
    Optional<Users> findByUsernameOrEmailAndIsDeleted(
            @Param("username") String username,
            @Param("email") String email,
            @Param("isDeleted") boolean isDeleted
    );

    @Query("SELECT u FROM Users u WHERE u.id=:id AND u.isDeleted = :isDeleted")
    Optional<Users> findByIdAndIsDeleted(@Param("id") String id, @Param("isDeleted") boolean isDeleted);

    @Query("SELECT u FROM Users u WHERE  u.isDeleted = :isDeleted")
    List<Users> getAllUsers(@Param("isDeleted") boolean isDeleted);

    @Query(value = "SELECT * FROM users WHERE is_deleted = :isDeleted AND " +
            "(:name <> '' AND (" +
            "COALESCE(LOWER(first_name), '') LIKE LOWER(CONCAT('%', :name, '%')) OR " +
            "COALESCE(LOWER(last_name), '') LIKE LOWER(CONCAT('%', :name, '%')) OR " +
            "COALESCE(LOWER(CONCAT(first_name, ' ', COALESCE(last_name, ''))), '') LIKE LOWER(CONCAT('%', :name, '%'))" +
            "))", nativeQuery = true)
    Page<Users> findByIsDeletedAndNameContaining(@Param("isDeleted") boolean isDeleted,
                                                 @Param("name") String name,
                                                 Pageable pageable);


    Page<Users> findByIsDeleted(boolean isDeleted, Pageable pageable);


}
