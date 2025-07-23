package com.spendless.auth.services;

import com.spendless.auth.models.Address;
import com.spendless.auth.dto.AddressDto;
import com.spendless.auth.payload.AddressPayload;


import java.util.List;

public interface AddressService {


      public List<AddressDto> getAllAddressByUserId(String userId, int page, int limit);

      public Address addAddressByUserId(AddressPayload payload, String userId);

      public Address getAddressByIdAndUserId(String addressId,String userId);



}
