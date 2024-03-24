package com.ons.securitylayerJwt.businessLogic;


import com.ons.securitylayerJwt.dto.LoginDto;
import com.ons.securitylayerJwt.dto.RegisterDto;

import com.ons.securitylayerJwt.dto.UserUpdateDto;
import com.ons.securitylayerJwt.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.Optional;


public interface IUserService  {


   //ResponseEntity<?> register (RegisterDto registerDto);
 //  ResponseEntity<BearerToken> authenticate(LoginDto loginDto);

  Map< String,String> authenticate(LoginDto loginDto);
   ResponseEntity<?> register (RegisterDto registerDto);


   User saverUser (User user) ;


    User updateUserDetails(String userId, UserUpdateDto userUpdateDto);
}
