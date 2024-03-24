package com.ons.securitylayerJwt.dto;

import lombok.Data;

@Data

public class UserUpdateDto {
    String id;
    String username ;
    String phonenum ;
    String email;
    String password ;

}
