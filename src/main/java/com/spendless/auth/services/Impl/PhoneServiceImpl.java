package com.spendless.auth.services.Impl;

import com.spendless.auth.models.Phone;
import com.spendless.auth.models.Users;
import com.spendless.auth.payload.PhonePayload;
import com.spendless.auth.repositories.PhoneRepository;
import com.spendless.auth.repositories.UserRepository;
import com.spendless.auth.services.PhoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class PhoneServiceImpl implements PhoneService {


    @Autowired
    private PhoneRepository phoneRepo;

    @Autowired
    private UserRepository userRepo;


    public Page<Phone> getAllPhonesByUserId(String id, Pageable page) {

        Page<Phone> phoneData = phoneRepo.findAllByUserId(id, page);

        if (!phoneData.isEmpty()) {
            return phoneData;
        } else return null;
    }


    public Phone addPhone(PhonePayload phoneDto, String id) {
        Optional<Users> user = userRepo.findById(id);

        if (user.isPresent()) {
            Users userOpt = user.get();
            Phone data = new Phone();
            data.setCountryCode(phoneDto.getCountryCode());
            data.setNumber(phoneDto.getNumber());
            data.setAlternateNumber(phoneDto.getAlternateNumber());
            data.setId(UUID.randomUUID().toString());
            data.setUser(userOpt);
            Phone savedPhone = phoneRepo.save(data);
            return savedPhone;
        } else {
            return null;
        }
    }

    public Phone getPhoneIdAndByUserId (String phoneId,String userId){
        Phone  userAndPhoneExist = phoneRepo.findByPhoneIdAndUserId(phoneId,userId);
        if(userAndPhoneExist != null){
          return userAndPhoneExist;
        }
        else return  null;
    }

    public Phone updatePhoneByIdAndUserId(String phoneId, String userId, PhonePayload payload){
       Phone isExist =  phoneRepo.findByPhoneIdAndUserId(phoneId,userId);
       if(isExist != null){
          isExist.setNumber(payload.getNumber());
          isExist.setAlternateNumber(payload.getAlternateNumber());
          isExist.setCountryCode(payload.getCountryCode());
          isExist.setUser(isExist.getUser());
          return phoneRepo.save(isExist);
       }
               return null;
    }

    public Integer deletePhoneById(String phoneId,String userId){
          Phone isUserExist = phoneRepo.findByPhoneIdAndUserId(phoneId,userId);
          if(isUserExist != null){
            phoneRepo.deleteById(phoneId);
            return 1;
          }
          else return 0;

    }


}



