package com.ons.securitylayerJwt.dto;


import lombok.Data;

@Data
public class BearerToken {

    private String accessToken ;
    private String userId ;

    public BearerToken(String accessToken , String userId) {
        this.userId = userId ;
        this.accessToken = accessToken;
    }


}
