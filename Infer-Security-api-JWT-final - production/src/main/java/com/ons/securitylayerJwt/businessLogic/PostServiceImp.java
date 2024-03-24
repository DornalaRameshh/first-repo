package com.ons.securitylayerJwt.businessLogic;

import com.ons.securitylayerJwt.models.Note;
import com.ons.securitylayerJwt.models.PostRating;
import com.ons.securitylayerJwt.models.Posts;
import com.ons.securitylayerJwt.persistence.PostsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PostServiceImp implements PostsService{
    @Autowired
   private PostsRepository postsRepository;
    @Override
    public Posts createPost(Posts posts) {

        if(posts.getPid()==null){
            posts.setPid(generateId());


        }


        return postsRepository.save(posts);

    }

    @Override
    public Posts addRatingToPost(String pid, int rating) {
        Optional<Posts> postOptional = postsRepository.findById(pid);

        if (postOptional.isPresent()) {
            Posts post = postOptional.get();

            // Create a new rating
            PostRating postRating = new PostRating();
            postRating.setRating(rating);
            postRating.setPost(post);

            // Add the rating to the post
            post.getRatings().add(postRating);

            // Update the post in the repository
            return postsRepository.save(post);
        } else {
            throw new RuntimeException("Post not found with id: " + pid);
        }
    }



    @Override
    public Posts addNoteToPost(String pid, String noteContent) {
        Optional<Posts> postOptional = postsRepository.findById(pid);

        if (postOptional.isPresent()) {
            Posts post = postOptional.get();

            // Check if the note already exists in the post's notes
            Optional<Note> existingNoteOptional = post.getNotes()
                    .stream()
                    .filter(note -> note.getContent().equals(noteContent))
                    .findFirst();

            if (existingNoteOptional.isPresent()) {
                // Note with the same content already exists, no need to add a new one
                return post;
            } else {
                // Create a new note
                Note note = new Note();
                note.setContent(noteContent);
                note.setPost(post);

                // Add the note to the post
                post.getNotes().add(note);

                // Update the post in the repository
                return postsRepository.save(post);
            }
        } else {
            throw new RuntimeException("Post not found with id: " + pid);
        }
    }


    @Override
    public boolean hasUserRatedPost(String postId, String userId) {
        Optional<Posts> postOptional = postsRepository.findById(postId);

        if (postOptional.isPresent()) {
            Posts post = postOptional.get();

            // Check if the post has any ratings
            List<PostRating> ratings = post.getRatings();
            return !ratings.isEmpty();
        }

        // Handle the case where the post with the given ID is not found
      return false;
    }



//        @Override
//        public Posts addNoteToPost(String pid, String noteContent) {
//            Optional<Posts> postOptional = postsRepository.findById(pid);
//
//            if (postOptional.isPresent()) {
//                Posts post = postOptional.get();
//
//                // Create a new note
//                Note note = new Note();
//                note.setContent(noteContent);
//                note.setPost(post);
//
//                // Add the note to the post
//                post.getNotes().add(note);
//
//                // Update the post in the repository
//                return postsRepository.save(post);
//            } else {
//                throw new RuntimeException("Post not found with id: " + pid);
//            }
       // }



    public String generateId(){
     String id= UUID.randomUUID().toString();
     return id;
    }
}
