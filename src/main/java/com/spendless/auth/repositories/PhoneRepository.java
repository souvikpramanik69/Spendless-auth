package com.spendless.auth.repositories;


import com.spendless.auth.models.Phone;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PhoneRepository extends JpaRepository<Phone,String> {

    Page<Phone> findAllByUserId(@Param("id")String id, Pageable pageable);


        @Query(value = "select p.* from users u inner join phone p on  p.user_id = u.id where p.id=:phoneId and u.id=:userId and u.is_deleted=false", nativeQuery = true)
        Phone findByPhoneIdAndUserId(@Param("phoneId") String phoneId, @Param("userId") String userId);




}
