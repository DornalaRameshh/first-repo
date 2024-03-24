package com.ons.securitylayerJwt.dto;

import lombok.Data;

@Data

public class PostResponseDTO {
    private String pid;

    private String title;
    private String content;
    private String author;
    private String link;


}
