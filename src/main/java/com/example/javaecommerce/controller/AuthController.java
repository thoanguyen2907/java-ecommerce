package com.example.javaecommerce.controller;

import com.example.javaecommerce.model.request.PasswordResetModel;
import com.example.javaecommerce.model.entity.UserEntity;
import com.example.javaecommerce.model.request.LoginRequest;
import com.example.javaecommerce.model.request.ResetEmail;
import com.example.javaecommerce.model.request.SignupRequest;

import com.example.javaecommerce.security.jwt.AuthEntryPointJwt;
import com.example.javaecommerce.services.UserService;
import com.example.javaecommerce.utils.JWTSecurity;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;


@RestController
@RequestMapping(path = "/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

    @PostMapping("/signin")
    public ResponseEntity<?> login(@RequestBody final LoginRequest loginRequest) {
        var jwtResponse = userService.login(loginRequest);
        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody final SignupRequest signupRequest, final HttpServletRequest httpServletRequest) {
        var newUser = userService.registerUser(signupRequest, httpServletRequest);
        return ResponseEntity.ok(newUser);
    }

    @GetMapping("/verifyRegistration")
    public String verifyRegistration(@RequestParam("token") final String token) {
        String result = userService.validateVerificationToken(token);
        if (result.equalsIgnoreCase("valid")) {
            return "User is verified successfully";
        }
        return "Failed to verify";
    }

    @PostMapping("/resetPassword")
    public void resetPassword(@RequestBody final ResetEmail resetEmail, final HttpServletRequest request) {
        UserEntity user = userService.findUserByEmail(resetEmail.getEmail());
        if (user != null) {
            String token = UUID.randomUUID().toString();
            var applicationUrl = JWTSecurity.applicationUrl(request);
            //send link url and password token to user email
            String urlLink = applicationUrl + "/api/auth/savePassword?token=" + token;
            userService.createPasswordResetTokenForUser(user, token, urlLink);

        }
    }

    @PostMapping("/savePassword")
    public String savePassword(@RequestParam("token") String token,
                               @RequestBody PasswordResetModel passwordResetModel) {
        String result = userService.validatePasswordResetToken(token);
        if (!result.equalsIgnoreCase("valid")) {
            return "Invalid Token";
        }
        UserEntity user = userService.getUserByPasswordResetToken(token);
        if (user != null) {
            userService.saveResetPassword(user, passwordResetModel);
            return "Password Reset Successfully";
        } else {
            return "Invalid Token";
        }
    }

    private String passwordResetTokenMail(UserEntity user, String applicationUrl, String token) {
        String url = applicationUrl + "/api/auth/savePassword?token=" + token;
        logger.info("Click the link to reset you password : {}", url);
        return url;
    }
}
