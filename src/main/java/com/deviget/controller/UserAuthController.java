package com.deviget.controller;

import com.deviget.component.JwtManager;
import com.deviget.domain.UserAuth;
import com.deviget.repository.UserAuthRepository;
import com.deviget.service.UserAuthService;
import com.deviget.types.SignInDTO;
import com.deviget.types.SignUpDTO;
import com.deviget.utils.PasswordStorage;
import com.deviget.utils.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/")
@Controller
public class UserAuthController {

    private UserAuthRepository userAuthRepository;

    private PasswordStorage passwordStorage;

    private JwtManager jwtManager;

    private UserAuthService userAuthService;

    @Autowired
    public void setUserAuthRepository(UserAuthRepository userAuthRepository) {
        this.userAuthRepository = userAuthRepository;
    }

    @Autowired
    public void setPasswordStorage(PasswordStorage passwordStorage) {
        this.passwordStorage = passwordStorage;
    }

    @Autowired
    public void setUserAuthService(UserAuthService userAuthService) {
        this.userAuthService = userAuthService;
    }

    @Autowired
    public void setJwtManager(JwtManager jwtManager) {
        this.jwtManager = jwtManager;
    }

    @PostMapping(path = "/userSignIn")
    public ResponseEntity userSignIn(@RequestBody SignInDTO signInDTO) {

        boolean isValid = Validator.isValidSignIn(signInDTO);
        if (!isValid) {
            return ResponseEntity.status(400).build();
        }
        try {
            UserAuth userAccount = userAuthRepository.findUserAuthByUsername(signInDTO.getUsername());
            boolean isVerifiedPassword = passwordStorage.verifyPassword(signInDTO.getPassword(), userAccount.getPassword());

            System.out.println("password verified" + isVerifiedPassword);
            if (!isVerifiedPassword) {
                return ResponseEntity.status(403).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(401).build();
        }

        String tokenResponse = jwtManager.generateToken(signInDTO.getUsername(), signInDTO.getPassword());


        //here userAuth Service take the account and generate token
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).header("token", tokenResponse).build();
    }


    @PostMapping(path = "/userSignUp")
    public ResponseEntity userSignUp(@RequestBody SignUpDTO signUpDTO) {

        boolean isValid = Validator.isValidSignUp(signUpDTO);
        if (!isValid) {
            return ResponseEntity.status(400).build();
        }
        String hash = "";
        try {
            hash = passwordStorage.createHash(signUpDTO.getPassword());
        } catch (Exception e) {
            return ResponseEntity.status(401).build();
        }
        UserAuth userAuth = new UserAuth(signUpDTO.getUsername(), hash, false);
        final UserAuth createdAuth = userAuthRepository.save(userAuth);
        System.out.println("Created user account");

        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(signUpDTO.toString());
    }


}