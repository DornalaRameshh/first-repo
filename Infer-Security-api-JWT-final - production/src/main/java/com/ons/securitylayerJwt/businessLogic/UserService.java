package com.ons.securitylayerJwt.businessLogic;

import com.ons.securitylayerJwt.dto.BearerToken;
import com.ons.securitylayerJwt.dto.LoginDto;
import com.ons.securitylayerJwt.dto.RegisterDto;

import com.ons.securitylayerJwt.dto.UserUpdateDto;
import com.ons.securitylayerJwt.models.User;

import com.ons.securitylayerJwt.persistence.IUserRepository;
import com.ons.securitylayerJwt.security.JwtUtilities;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@Transactional
@RequiredArgsConstructor
public class UserService implements IUserService{

    private final AuthenticationManager authenticationManager ;
    private final IUserRepository iUserRepository ;

    private final PasswordEncoder passwordEncoder ;
    private final JwtUtilities jwtUtilities ;

    private final IUserRepository userRepository;





//    @Override
//    public Role saveRole(Role role) {
//        return iRoleRepository.save(role);
//    }

    @Override
    public User saverUser(User user) {

        return iUserRepository.save(user);
    }

    @Override
    public User updateUserDetails(String userId, UserUpdateDto userUpdateDto) {
        Optional<User> userOptional = iUserRepository.findById(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Update the user properties based on the userUpdateDto
            if (userUpdateDto.getUsername() != null && !userUpdateDto.getUsername().isEmpty()) {
                user.setUsername(userUpdateDto.getUsername());
            }

            if (userUpdateDto.getEmail() != null && !userUpdateDto.getEmail().isEmpty()) {
                user.setEmail(userUpdateDto.getEmail());
            }

            if (userUpdateDto.getPhonenum() != null && !userUpdateDto.getPhonenum().isEmpty()) {
                user.setPhonenum(userUpdateDto.getPhonenum());
            }
            if (userUpdateDto.getPassword() != null && !userUpdateDto.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(userUpdateDto.getPassword()));
            }



            // Add logic for other fields

            // Save the updated user
            return iUserRepository.save(user);
        } else {
            // Handle the case where the user with the given ID is not found
            throw new RuntimeException("User not found with ID: " + userId);
        }
    }




    @Override
    public ResponseEntity<?> register(RegisterDto registerDto) {
        if(iUserRepository.existsByEmail(registerDto.getEmail()))
        { return  new ResponseEntity<>("email is already taken !", HttpStatus.SEE_OTHER); }
        else
        { User user = new User();
            if(registerDto.getId()==null){

            }
            user.setId(generateRandomId());
            user.setEmail(registerDto.getEmail());
            user.setPhonenum(registerDto.getPhonenum());
            user.setUsername(registerDto.getUsername());
            user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
            String id=user.getId();


            iUserRepository.save(user);
            Map<String,String> tokens=new HashMap<>();
            String accessToken = jwtUtilities.generateToken(registerDto.getEmail());
            String refreshToken =jwtUtilities.generateRefreshToken(registerDto.getEmail());
           // return new ResponseEntity<>(new BearerToken(token , id),HttpStatus.OK);
            tokens.put("accessToken :",accessToken);
            tokens.put("refreshToken :",refreshToken);
            tokens.put("userId :",id);
            return ResponseEntity.ok(tokens);

        }

        }
    private String generateRandomId() {
        UUID uuid = UUID.randomUUID();
        String s= uuid.toString();

        return s;
        }

    @Override
    public Map<String,String> authenticate(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getEmail(),
                        loginDto.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = iUserRepository.findByEmail(authentication.getName()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        // List<String> rolesNames = new ArrayList<>();
//        user.getRoles().forEach(r-> rolesNames.add(r.getRoleName()));
//        String accessToken = jwtUtilities.generateToken(user.getUsername());
//        String refreshToken = jwtUtilities.generateRefreshToken(user.getUsername());
        String accessToken = jwtUtilities.generateToken(user.getUsername());
        String refreshToken = jwtUtilities.generateRefreshToken(user.getUsername());


        Optional<User> userOptional = userRepository.findByEmail(loginDto.getEmail());
        User user1 = userOptional.get();
        Map<String, String> tokens = new HashMap<>();
        Map<String,String> errorRes=new HashMap<>();
        errorRes.put("userId","not found");

        if (userOptional.isPresent()) {

            String id = user1.getId();
            tokens.put("userId", id);
        }
        else {
            return errorRes;
        }






            tokens.put("access_token", accessToken);
            tokens.put("refresh_token", refreshToken);
            //tokens.put("userId", id);
            return tokens;
        }






}

