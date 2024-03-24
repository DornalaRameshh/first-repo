package com.ons.securitylayerJwt.presentation;


import com.ons.securitylayerJwt.businessLogic.IUserService;
import com.ons.securitylayerJwt.businessLogic.ProfileService;
import com.ons.securitylayerJwt.businessLogic.UserConvService;
import com.ons.securitylayerJwt.dto.LoginDto;
import com.ons.securitylayerJwt.dto.RegisterDto;
import com.ons.securitylayerJwt.dto.UserResponseDto;
import com.ons.securitylayerJwt.dto.UserUpdateDto;
import com.ons.securitylayerJwt.models.User;
import com.ons.securitylayerJwt.persistence.IUserRepository;
import com.ons.securitylayerJwt.security.JwtUtilities;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserRestController {

    private final IUserService iUserService;
    private final JwtUtilities jwtTokenUtil;
    @Autowired
    private final IUserRepository userRepository;

@Autowired
private ProfileService profileService;

@Autowired
private UserConvService userConvService;


    //RessourceEndPoint:http://localhost:8080/api/user/register
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDto registerDto) {
        return iUserService.register(registerDto);
    }


    //RessourceEndPoint:http://localhost:8087/api/user/authenticate
    @PostMapping("/login")
    public Map<String, String> authenticate(@RequestBody LoginDto loginDto) {
//
//        String email=loginDto.getEmail();
//        Optional<User> userOptional = userRepository.findByEmail(email);
//        User user=userOptional.get();
//        String id=user.getId();

        return iUserService.authenticate(loginDto);
    }




//    @PostMapping("/authenticate")
//    public Map<String, String> authenticate(@RequestBody LoginDto loginDto) {
//        Optional<User> userOptional = userRepository.findByEmail(loginDto.getEmail());
//
//        if (userOptional.isPresent()) {
//            User user = userOptional.get();
//            String userId = user.getId();
//
//            // Authenticate the user and get the authentication token
//            String authToken = iUserService.authenticate(loginDto);
//
//            // Create a map to hold the response values
//            Map<String, String> response = new HashMap<>();
//            response.put("userId", userId);
//            response.put("token", authToken);
//
//            return response;
//        } else {
//            // Handle the case where the user is not found
//            throw new UserNotFoundException("User not found with email: " + loginDto.getEmail());
//        }
//    }

//    @GetMapping("/getUser/{id}")
//    public ResponseEntity<User> getUserDetails(@PathVariable String id) {
//        try {
//            Optional<User> user = userRepository.findById(id);
//
//            return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.badRequest().build();
//        }
//    }
@GetMapping("/getUser/{id}")
public ResponseEntity<UserResponseDto> getUserById(@PathVariable String id) {
    // Retrieve user from the database (or wherever)
    Optional<User> user = userRepository.findById(id);
    User user1=user.get();

    // Convert user entity to DTO using the service method
    UserResponseDto userDTO = userConvService.convertToDTO(user1);

    return new ResponseEntity<>(userDTO, HttpStatus.OK);
}

    @PutMapping("/updateUser/{userId}")
    public ResponseEntity<UserResponseDto> updateUserDetails(
            @PathVariable String userId,
            @RequestBody UserUpdateDto userUpdateDto) {
        // Your implementation to update user details
        // userUpdateDto should contain the updated information

        // Example: Update user details using a service method
        User updatedUser = iUserService.updateUserDetails(userId, userUpdateDto);

        // Convert the updated user to DTO and return the response
        UserResponseDto updatedUserDto = userConvService.convertToDTO(updatedUser);
        return ResponseEntity.ok(updatedUserDto);
    }

    @GetMapping("/login")
    public ResponseEntity<Object> getRes(@RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token Is required to Proceed");
        } else {
            String realToken = token.substring(7);
            boolean tokenCheckResult = jwtTokenUtil.validateToken(realToken);

            if (tokenCheckResult) {
                // Extract the subject (sub) from the token
                String username = jwtTokenUtil.extractUsername(realToken);


                Optional<User> userOptional = userRepository.findByEmail(username);
                //User user1=new User();

                if (userOptional.isPresent()) {
                    User user =userOptional.get();

                String rid = " user UUId:" + user.getId();
//                    //return new ResponseEntity<>( username,HttpStatus.OK);

                    return  ResponseEntity.ok(rid);
                } else {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
                }
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized due to: " + tokenCheckResult);
            }
        }
    }



    @PostMapping("/add-picture/{userId}")
    public ResponseEntity<?> uploadProfilePicture(@PathVariable String userId,
                                                      @RequestParam(value="file") MultipartFile profilePicture) throws IOException {
        Map<String, String> response=new HashMap<>();





       User user= profileService.saveUserProfilePicture(userId, profilePicture);
       response.put("response :", "Profile Updated Successfully");

        response.put("profileImageUrl :",user.getProfilePicUrl());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete-picture/{userId}")
    public ResponseEntity<?> deleteProfilePicture(@PathVariable String userId) {
        profileService.deleteUserProfilePicture(userId);
        Map<String ,String> response= new HashMap<>();
        response.put("response :", "profile image successfully deleted");

        return ResponseEntity.ok(response);
    }







}
