package com.spendless.auth.repositories;


import com.spendless.auth.models.Address;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AddressRepositories extends JpaRepository<Address,String> {

    Page<Address> findAllByUserId(@Param("id")String id, Pageable pageable);

    Optional<Address> findByUserId(@Param("userId") String userId);

    Optional<Address> findByIdAndUserId(@Param("addressId") String addressId,@Param("userId") String userId);



}
