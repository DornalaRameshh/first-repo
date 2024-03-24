package com.ons.securitylayerJwt.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.*;


@Entity
@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
//@FieldDefaults(level = AccessLevel.PRIVATE)
public class User implements Serializable , UserDetails {

    @Id
    String id ;

    @Column(name = "username")
   private String username;
    String email;
    String phonenum ;
    String password ;
    String profilePicUrl;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL,fetch = FetchType.EAGER,orphanRemoval = true)
  //  @JsonIgnore  // Add this annotation to break the circular reference
    //@JsonManagedReference
    private List<Posts> posts = new ArrayList<>();



    public User (String email , String password ) {
      this.email= email ;
      this.password=password ;
      }

    public String getId() {
        return id;
    }



    public String getUsername(){

        return this. email;
    }

    public String getEmail(){

        return this.username;
    }
    public void setEmail(String email){
        this.email=email;



    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        //this.roles.forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getRoleName())));
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
