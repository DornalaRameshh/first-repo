package com.ons.securitylayerJwt.businessLogic;

import com.ons.securitylayerJwt.models.Posts;

public interface PostsService {
    public Posts createPost(Posts posts);
    Posts addRatingToPost(String pid, int rating);
    Posts addNoteToPost(String pid, String noteContent);
    public boolean hasUserRatedPost(String postId, String userId);

}
