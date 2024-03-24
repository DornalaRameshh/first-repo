package com.ons.securitylayerJwt;

//import com.ons.securitylayerJwt.businessLogic.IUserService;
//import com.ons.securitylayerJwt.models.Role;
//import com.ons.securitylayerJwt.models.RoleName;
//import com.ons.securitylayerJwt.models.User;
//import com.ons.securitylayerJwt.persistence.IRoleRepository;
//import com.ons.securitylayerJwt.persistence.IUserRepository;
//import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SpringBootApplication
public class SecurityLayerJwtApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityLayerJwtApplication.class, args);
    }


}

