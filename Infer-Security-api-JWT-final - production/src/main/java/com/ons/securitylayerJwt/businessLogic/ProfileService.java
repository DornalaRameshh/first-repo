package com.ons.securitylayerJwt.businessLogic;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.ons.securitylayerJwt.dto.UserUpdateDto;
import com.ons.securitylayerJwt.models.User;
import com.ons.securitylayerJwt.persistence.IUserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;
@Service

public class ProfileService {

    private Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private AmazonS3 amazonS3;

    @Value("${aws.s3.bucketName}")
    private String bucketName;



    public User saveUserProfilePicture(String userId, MultipartFile profilePicture) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        // Upload profile picture to S3 and update the user's profilePicUrl
       /* String key = "profile-pics/" + userId + "/" + profilePicture.getOriginalFilename();
        amazonS3.putObject(bucketName, key, profilePicture.getInputStream(), new ObjectMetadata());
        String imageUrl = amazonS3.getUrl(bucketName, key).toString();

        user.setProfilePicUrl(imageUrl);

        // Update the user in the database with the S3 image URL
        return userRepository.save(user);

        */
        String uniqueFilename = UUID.randomUUID().toString() + ".jpg";

        // Upload profile picture to S3 and update the user's profilePicUrl
        String key = "profile-pics/" + userId + "/" + uniqueFilename;
        amazonS3.putObject(bucketName, key, profilePicture.getInputStream(), new ObjectMetadata());
        String imageUrl = amazonS3.getUrl(bucketName, key).toString();

        user.setProfilePicUrl(imageUrl);

        // Update the user in the database with the S3 image URL
        return userRepository.save(user);
    }



    public User deleteUserProfilePicture(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        String profilePicUrl = user.getProfilePicUrl();
        if (profilePicUrl != null) {
            String key = extractKeyFromUrl(profilePicUrl);

            // Log the key to verify it matches the expected format
            System.out.println("Deleting S3 object with key: " + key);

            try {
                amazonS3.deleteObject(new DeleteObjectRequest(bucketName, key));
            } catch (AmazonS3Exception e) {
                System.err.println("Error deleting S3 object: " + e.getMessage());
                // Handle the exception as needed
            }
        }


        // Update user's profilePicUrl to null
        user.setProfilePicUrl(null);

        // Save the updated user to the database
        return userRepository.save(user);





    }




    private String extractKeyFromUrl(String imageUrl) {
        // Assuming the key is the part after the last "/"
        int lastSlashIndex = imageUrl.lastIndexOf("/");
        if (lastSlashIndex != -1 && lastSlashIndex < imageUrl.length() - 1) {
            return imageUrl.substring(lastSlashIndex + 1);
        } else {
            throw new IllegalArgumentException("Invalid S3 URL format: " + imageUrl);
        }
    }

}


