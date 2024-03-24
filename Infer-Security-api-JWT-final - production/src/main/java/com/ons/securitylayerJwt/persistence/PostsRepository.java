package com.ons.securitylayerJwt.persistence;

import com.ons.securitylayerJwt.models.Posts;
import com.ons.securitylayerJwt.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface PostsRepository extends JpaRepository<Posts,String> {
    Posts save(Posts post);
    Optional<Posts> findById(String id);


    void deleteById(String id);

    List<Posts> getAllPostsByUser(User currentUser);


}
