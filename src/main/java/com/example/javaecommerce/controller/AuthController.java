package com.example.javaecommerce.controller;

import com.example.javaecommerce.model.request.PasswordResetModel;
import com.example.javaecommerce.model.request.LoginRequest;
import com.example.javaecommerce.model.request.ResetEmail;
import com.example.javaecommerce.model.request.SignupRequest;

import com.example.javaecommerce.security.jwt.AuthEntryPointJwt;
import com.example.javaecommerce.services.UserService;
;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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
        userService.checkAndCreatePasswordResetTokenForUser(resetEmail, request);
    }

    @PostMapping("/savePassword")
    public ResponseEntity<?> savePassword(@RequestParam("token")final  String token,
                                          @RequestBody final PasswordResetModel passwordResetModel) {
        userService.resetPassword(token, passwordResetModel);
        return ResponseEntity.ok("Password reset successfully");
    }

}
