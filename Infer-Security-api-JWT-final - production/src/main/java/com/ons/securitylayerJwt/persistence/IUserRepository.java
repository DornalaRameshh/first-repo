package com.ons.securitylayerJwt.persistence;

import com.ons.securitylayerJwt.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface IUserRepository extends JpaRepository<User,Integer> {

    Boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
    Optional<User> findById(String id);

   // User findByUsername(String username);
    //String findIdByEmail(@Param("email") String email);
   // Query("SELECT u.id FROM User u WHERE u.email = :email")
   // String findByUserName(@Param("username") String username);


}


