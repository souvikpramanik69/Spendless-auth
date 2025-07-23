package com.spendless.auth.services;


import com.spendless.auth.models.Phone;
import com.spendless.auth.payload.PhonePayload;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PhoneService {

   Page<Phone> getAllPhonesByUserId(String id, Pageable page);
   public Phone addPhone(PhonePayload phoneDto, String id);
   public Phone getPhoneIdAndByUserId(String phoneId,String userId);
   public Phone updatePhoneByIdAndUserId(String phoneId, String userId, PhonePayload payload);
   public Integer deletePhoneById(String phoneId,String userId);


}
