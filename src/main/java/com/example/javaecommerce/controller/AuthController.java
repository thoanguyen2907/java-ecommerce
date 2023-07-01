package com.example.javaecommerce.controller;

import com.example.javaecommerce.event.RegistrationCompleteEvent;
import com.example.javaecommerce.model.request.LoginRequest;
import com.example.javaecommerce.model.request.SignupRequest;

import com.example.javaecommerce.services.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping(path = "/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final ApplicationEventPublisher publisher;

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
    public String verifyRegistration(@RequestParam("token") String token){
        String result = userService.validateVerificationToken(token);
        if(result.equalsIgnoreCase("valid")){
            return "User is verified successfully";
        }
        return "Failed to verify";
    }

}
