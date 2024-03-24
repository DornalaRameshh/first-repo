package com.ons.securitylayerJwt.dto;

import lombok.Data;

import java.util.List;

@Data

public class UserResponseDto {
    private String id;
    private String username;
    private String email;
    private String phonenum;
    private String password;
    private String profilePicUrl;
    private List<PostResponseDTO> posts;




}
