package com.spendless.auth.services.Impl;

import com.spendless.auth.models.Address;
import com.spendless.auth.models.Users;
import com.spendless.auth.dto.AddressDto;
import com.spendless.auth.mapper.AddressMapper;
import com.spendless.auth.payload.AddressPayload;
import com.spendless.auth.repositories.AddressRepositories;
import com.spendless.auth.repositories.UserRepository;
import com.spendless.auth.services.AddressService;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressRepositories repositories;

    @Autowired
    UserRepository userRepository;

    public List<AddressDto> getAllAddressByUserId(String userId, int page, int limit){
        Pageable pageable = PageRequest.of(page-1,limit);
        Page<Address> userPage = repositories.findAllByUserId(userId,pageable);
       if(!userPage.isEmpty()){
           List<Address> addressList = userPage.stream().toList();
           List<AddressDto> dtoList = addressList.stream().map((item)->{
               AddressDto dto = AddressMapper.mapToDto(item);
               return dto;
           }).toList();
           return dtoList;
       }
       else return null;
    }

    public Address addAddressByUserId(AddressPayload payload, String userId){
         Optional<Users> isUserExist = userRepository.findByIdAndIsDeleted(userId,false);
         if(isUserExist.isPresent()){
             Address newAddress = new Address();
             newAddress.setAddress1(payload.getAddress1());
             newAddress.setAddress2(payload.getAddress2());
             newAddress.setCity(payload.getCity());
             newAddress.setPostal_code(payload.getPostal_code());
             newAddress.setState(payload.getState());
             newAddress.setId(UUID.randomUUID().toString());
             newAddress.setUser(isUserExist.get());
             return repositories.save(newAddress);
         }
         return null;
    }


    public Address getAddressByIdAndUserId(String addressId,String userId){
         Optional<Address> isUserExist = repositories.findByIdAndUserId(addressId,userId);
        return isUserExist.orElse(null);
    }
    


}
