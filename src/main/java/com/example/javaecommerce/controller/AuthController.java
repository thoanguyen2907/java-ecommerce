package com.example.javaecommerce.controller;

import com.example.javaecommerce.model.ERole;
import com.example.javaecommerce.model.entity.RoleEntity;
import com.example.javaecommerce.model.entity.UserEntity;
import com.example.javaecommerce.model.request.LoginRequest;
import com.example.javaecommerce.model.request.SignupRequest;
import com.example.javaecommerce.model.response.JwtResponse;
import com.example.javaecommerce.model.response.MessageResponse;
import com.example.javaecommerce.repository.RoleRepository;
import com.example.javaecommerce.repository.UserRepository;
import com.example.javaecommerce.security.jwt.JwtUtils;
import com.example.javaecommerce.security.services.UserDetailsImpl;
import com.example.javaecommerce.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "api/auth")
@RequiredArgsConstructor
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserService userService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
      var jwtResponse = userService.login(loginRequest);
        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signupRequest) {
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse(
                    "Error : Username is already taken !!"
            ));
        }
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            return ResponseEntity.badRequest().body(
                    new MessageResponse("Error : Email is already in use! "));
        }
        UserEntity user = new UserEntity(signupRequest.getUsername(),
                signupRequest.getEmail(),
                passwordEncoder.encode(signupRequest.getPassword()));
        Set<String> strRoles = signupRequest.getRole();
        Set<RoleEntity> roles = new HashSet<>();
        if (strRoles == null) {
            RoleEntity userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error : Role is not found "));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        RoleEntity adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error : Role is not found"));
                        roles.add(adminRole);
                        break;
                    case "mod":
                        RoleEntity modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error : Role is not found"));
                        roles.add(modRole);
                        break;
                    default:
                        RoleEntity userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error : Role is not found"));
                        roles.add(userRole);
                }
            });
        }
        user.setRoles(roles);
        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("User registered successfully"));
    }
}
