package com.ons.securitylayerJwt.presentation;


import com.ons.securitylayerJwt.businessLogic.PostsService;
import com.ons.securitylayerJwt.models.Posts;
import com.ons.securitylayerJwt.models.User;
import com.ons.securitylayerJwt.persistence.IUserRepository;
import com.ons.securitylayerJwt.persistence.PostsRepository;
import com.ons.securitylayerJwt.security.JwtUtilities;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostsService postService;

    @Autowired
    private JwtUtilities jwtTokenUtil;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private PostsRepository postsRepository;

    @PostMapping("/savePost")
    public ResponseEntity<?> createPost(@RequestHeader("Authorization") String token, @RequestBody Posts post) {
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token Is required to Proceed");
        } else {
            String realToken = token.substring(7);
            boolean tokenCheckResult = jwtTokenUtil.validateToken(realToken);

            if (tokenCheckResult) {
                // Extract the subject (sub) from the token
                String username = jwtTokenUtil.extractUsername(realToken);

                // Find the user by username
                Optional<User> currentUserOptional = userRepository.findByEmail(username);

                if (currentUserOptional.isPresent()) {
                    User currentUser = currentUserOptional.get();

                    // Associate the post with the current user
                    post.setUser(currentUser);

                    // Save the post
                    Posts createdPost = postService.createPost(post);
                    return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
                } else {
                    // User not found, return unauthorized
                    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
                }
            } else {
                // Invalid token, return unauthorized
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        }
    }

    @GetMapping("/getPosts/{id}")
    public ResponseEntity<Posts> getUserDetails(@PathVariable String id) {
        try {
            Optional<Posts> posts = postsRepository.findById(id);

            return posts.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }


    @PutMapping("/updatePost/{postId}")
    public ResponseEntity<?> updatePost(
            @RequestHeader("Authorization") String token,
            @PathVariable String postId,
            @RequestBody Posts updatedPost) {

        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token Is required to Proceed");
        } else {
            String realToken = token.substring(7);
            boolean tokenCheckResult = jwtTokenUtil.validateToken(realToken);

            if (tokenCheckResult) {
                // Extract the subject (sub) from the token
                String username = jwtTokenUtil.extractUsername(realToken);

                // Find the user by username
                Optional<User> currentUserOptional = userRepository.findByEmail(username);

                if (currentUserOptional.isPresent()) {
                    User currentUser = currentUserOptional.get();

                    // Check if the post belongs to the current user
                    Optional<Posts> existingPostOptional = postsRepository.findById(postId);
                    if (existingPostOptional.isPresent()) {
                        Posts existingPost = existingPostOptional.get();
                        if (existingPost.getUser().equals(currentUser)) {
                            // Update the post with new data
                            existingPost.setTitle(updatedPost.getTitle());
                            existingPost.setContent(updatedPost.getContent());

                            // Save the updated post
                            Posts updatedPostEntity = postService.createPost(existingPost);
                            return new ResponseEntity<>(updatedPostEntity, HttpStatus.OK);
                        } else {
                            // Post does not belong to the current user
                            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You don't have permission to update this post");
                        }
                    } else {
                        // Post not found
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found");
                    }
                } else {
                    // User not found, return unauthorized
                    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
                }
            } else {
                // Invalid token, return unauthorized
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        }


    }
//    @Transactional
//    @DeleteMapping("/deletePost/{postId}")
//    public ResponseEntity<?> deletePost(
//            @RequestHeader("Authorization") String token,
//            @PathVariable String postId) {
//
//        if (token == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token Is required to Proceed");
//        } else {
//            String realToken = token.substring(7);
//            boolean tokenCheckResult = jwtTokenUtil.validateToken(realToken);
//
//            if (tokenCheckResult) {
//                // Extract the subject (sub) from the token
//                String username = jwtTokenUtil.extractUsername(realToken);
//
//                // Find the user by username
//                Optional<User> currentUserOptional = userRepository.findByEmail(username);
//
//                if (currentUserOptional.isPresent()) {
//                    User currentUser = currentUserOptional.get();
//
//                    // Check if the post belongs to the current user
//                    Optional<Posts> existingPostOptional = postsRepository.findById(postId);
//                    if (existingPostOptional.isPresent()) {
//                        Posts existingPost = existingPostOptional.get();
//                        if (existingPost.getUser().equals(currentUser)) {
//                            // Delete the post
//                            postsRepository.deleteById(postId);
//                            return ResponseEntity.status(HttpStatus.OK).body("Post deleted successfully");
//                        } else {
//                            // Post does not belong to the current user
//                            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You don't have permission to delete this post");
//                        }
//                    } else {
//                        // Post not found
//                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found");
//                    }
//                } else {
//                    // User not found, return unauthorized
//                    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
//                }
//            } else {
//                // Invalid token, return unauthorized
//                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
//            }
//        }
//    }


    @GetMapping("/getUserPosts")
    public ResponseEntity<?> getUserPosts(@RequestHeader("Authorization") String token) {
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token Is required to Proceed");
        } else {
            String realToken = token.substring(7);
            boolean tokenCheckResult = jwtTokenUtil.validateToken(realToken);

            if (tokenCheckResult) {
                // Extract the subject (sub) from the token
                String username = jwtTokenUtil.extractUsername(realToken);

                // Find the user by username
                Optional<User> currentUserOptional = userRepository.findByEmail(username);

                if (currentUserOptional.isPresent()) {
                    User currentUser = currentUserOptional.get();

                    // Get all posts of the current user
                    List<Posts> userPosts = postsRepository.getAllPostsByUser(currentUser);

                    return new ResponseEntity<>(userPosts, HttpStatus.OK);
                } else {
                    // User not found, return unauthorized
                    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
                }
            } else {
                // Invalid token, return unauthorized
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        }
    }

    ///__________________________________ rating________________

    @PostMapping("/ratePost/{postId}/{rating}")
    public ResponseEntity<?> ratePost(
            @RequestHeader("Authorization") String token,
            @PathVariable String postId,
            @PathVariable int rating) {

        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token Is required to Proceed");
        } else {
            String realToken = token.substring(7);
            boolean tokenCheckResult = jwtTokenUtil.validateToken(realToken);

            if (tokenCheckResult) {
                // Extract the subject (sub) from the token
                String username = jwtTokenUtil.extractUsername(realToken);

                // Find the user by username
                Optional<User> currentUserOptional = userRepository.findByEmail(username);

                if (currentUserOptional.isPresent()) {
                    User currentUser = currentUserOptional.get();

                    // Check if the post belongs to the current user
                    Optional<Posts> existingPostOptional = postsRepository.findById(postId);
                    boolean hasRated = postService.hasUserRatedPost(postId, username);
                    if (existingPostOptional.isPresent()) {
                        if (!hasRated) {


                            // Add the rating to the post
                            Posts updatedPost = postService.addRatingToPost(postId, rating);
                            return new ResponseEntity<>(updatedPost, HttpStatus.OK);
                        } else {
                            // Post not found
                            return ResponseEntity.ok("you rated this post already");
                        }
                    } else {
                        // User not found, return unauthorized
                        return ResponseEntity.ok("post not found");
                    }
                } else {
                    // Invalid token, return unauthorized
                    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
                }
            }
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    //_________________________________adding Notes to the Post_________________
    @PostMapping("/addNote/{postId}")
    public ResponseEntity<?> addNoteToPost(
            @RequestHeader("Authorization") String token,
            @PathVariable String postId,
            @RequestBody String noteContent) {

        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token Is required to Proceed");
        } else {
            String realToken = token.substring(7);
            boolean tokenCheckResult = jwtTokenUtil.validateToken(realToken);

            if (tokenCheckResult) {
                // Extract the subject (sub) from the token
                String username = jwtTokenUtil.extractUsername(realToken);

                // Find the user by username
                Optional<User> currentUserOptional = userRepository.findByEmail(username);

                if (currentUserOptional.isPresent()) {
                    User currentUser = currentUserOptional.get();

                    // Check if the post belongs to the current user
                    Optional<Posts> existingPostOptional = postsRepository.findById(postId);
                    if (existingPostOptional.isPresent()) {
                        // Add the note to the post
                        Posts updatedPost = postService.addNoteToPost(postId, noteContent);
                        return new ResponseEntity<>(updatedPost, HttpStatus.OK);
                    } else {
                        // Post not found
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found");
                    }
                } else {
                    // User not found, return unauthorized
                    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
                }
            } else {
                // Invalid token, return unauthorized
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        }
    }

    @DeleteMapping("/deletePost/{userId}/{postId}")
    public ResponseEntity<?> deletePost(
            @PathVariable String userId,
            @PathVariable String postId) {

        // Find the user by userId
        Optional<User> currentUserOptional = userRepository.findById(userId);

        if (currentUserOptional.isPresent()) {
            User currentUser = currentUserOptional.get();

            // Check if the post belongs to the current user
            Optional<Posts> existingPostOptional = postsRepository.findById(postId);
            if (existingPostOptional.isPresent()) {
                Posts existingPost = existingPostOptional.get();
                if (existingPost.getUser().equals(currentUser)) {
                    // Delete the post
                    postsRepository.deleteById(postId);

                    // Optionally, you can perform additional cleanup (e.g., remove from user's posts list)
                    currentUser.getPosts().remove(existingPost);
                    userRepository.save(currentUser);

                    return ResponseEntity.status(HttpStatus.OK).body("Post deleted successfully");
                } else {
                    // Post does not belong to the current user
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You don't have permission to delete this post");
                }
            } else {
                // Post not found
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found");
            }
        } else {
            // User not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

}
