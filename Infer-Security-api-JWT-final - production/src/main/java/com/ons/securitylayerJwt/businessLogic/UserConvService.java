package com.ons.securitylayerJwt.businessLogic;

import com.ons.securitylayerJwt.dto.PostResponseDTO;
import com.ons.securitylayerJwt.dto.UserResponseDto;
import com.ons.securitylayerJwt.models.Posts;
import com.ons.securitylayerJwt.models.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class UserConvService {

    public UserResponseDto convertToDTO(User user) {
        UserResponseDto dto = new UserResponseDto();
        // map properties from User to UserResponseDTO
        dto.setId(user.getId());
        dto.setUsername(user.getEmail());
        dto.setEmail(user.getUsername());
        dto.setPhonenum(user.getPhonenum());
        dto.setPassword(user.getPassword());
        dto.setProfilePicUrl(user.getProfilePicUrl());

        // Assuming you have a similar method to convert Post entities to DTOs
        dto.setPosts(convertPostsToDTO(user.getPosts()));
        return dto;
}

    private List<PostResponseDTO> convertPostsToDTO(List<Posts> posts) {
        List<PostResponseDTO> postDTOList = new ArrayList<>();

        for (Posts post : posts) {
            PostResponseDTO postDTO = new PostResponseDTO();
            postDTO.setPid(post.getPid());
            postDTO.setTitle(post.getTitle());
            postDTO.setContent(post.getContent());
            postDTO.setAuthor(post.getAuthor());
            postDTO.setLink(post.getLink());



            postDTOList.add(postDTO);
        }

        return postDTOList;

    }
    }
