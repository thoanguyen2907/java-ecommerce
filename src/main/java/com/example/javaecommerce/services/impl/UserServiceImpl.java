package com.example.javaecommerce.services.impl;

import com.example.javaecommerce.exception.ResourceNotFoundException;
import com.example.javaecommerce.mapper.UserMapper;
import com.example.javaecommerce.model.ERole;
import com.example.javaecommerce.model.entity.RoleEntity;
import com.example.javaecommerce.model.entity.UserEntity;
import com.example.javaecommerce.model.request.LoginRequest;
import com.example.javaecommerce.model.request.SignupRequest;
import com.example.javaecommerce.model.request.UserRequest;
import com.example.javaecommerce.model.response.JwtResponse;
import com.example.javaecommerce.model.response.MessageResponse;
import com.example.javaecommerce.model.response.UserResponse;
import com.example.javaecommerce.pagination.OffsetPageRequest;
import com.example.javaecommerce.pagination.PaginationPage;
import com.example.javaecommerce.repository.RoleRepository;
import com.example.javaecommerce.repository.UserRepository;
import com.example.javaecommerce.security.jwt.JwtUtils;
import com.example.javaecommerce.security.services.UserDetailsImpl;
import com.example.javaecommerce.services.UserService;

import javax.transaction.Transactional;

import com.example.javaecommerce.utils.JWTSecurity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;

    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    @Override
    public List<UserResponse> getAllUsers() {
        List<UserEntity> userEntities = (List<UserEntity>) userRepository.findAll();
        return userMapper.toListUserResponse(userEntities);
    }

    @Override
    public UserResponse addUser(UserRequest userRequest) {
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new IllegalStateException("Email is already existed !!");
        }
        UserEntity user = new UserEntity();
        user.setEmail(userRequest.getEmail());
        Set<String> strRoles = userRequest.getRoles();
        Set<RoleEntity> roles = new HashSet<>();
        strRoles.forEach(role -> {
            switch (role) {
                case "admin":
                    RoleEntity adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                            .orElseThrow(() -> new ResourceNotFoundException("Role", "id", ERole.ROLE_ADMIN));
                    roles.add(adminRole);
                    break;
                case "moderator":
                    RoleEntity therapistRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                            .orElseThrow(() -> new ResourceNotFoundException("Role", "id", ERole.ROLE_MODERATOR));
                    roles.add(therapistRole);
                    break;
                default:
                    RoleEntity userRole = roleRepository.findByName(ERole.ROLE_USER)
                            .orElseThrow(() -> new ResourceNotFoundException("Role", "id", ERole.ROLE_USER));
                    roles.add(userRole);
            }
        });
        //user.setRoles(roles);
        UserEntity result = userRepository.save(user);
        return userMapper.toUserResponse(result);
    }

    @Override
    public void deleteUser(Long userID) {
        userRepository.deleteById(userID);
    }

    @Override
    public UserResponse updateUser(UserRequest userRequest, Long id) {
        UserEntity userEntity = userRepository.findById(id)
                .map(user -> {
                    user.setEmail(userRequest.getEmail());
                    user.setPassword(userRequest.getPassword());
                    return userRepository.save(user);
                }).orElseThrow(() -> new ResourceNotFoundException("user", "id", id));
        return userMapper.toUserResponse(userEntity);
    }

    @Override
    public PaginationPage<UserResponse> getUserByPagination(Integer offset, Integer limited) {
        var pageable = new OffsetPageRequest(offset, limited);
        var userList = userRepository.findAll(pageable);
        var userResponse = userList
                .getContent()
                .stream()
                .map(item -> userMapper.toUserResponse(item))
                .collect(Collectors.toList());
        return new PaginationPage<UserResponse>()
                .setOffset(userList.getNumber())
                .setTotalRecords(userList.getTotalElements())
                .setRecords(userResponse)
                .setLimit(userList.getSize());
    }

    @Override
    public UserResponse aboutMe() {
        var signedInUser = JWTSecurity.getJWTUserInfo().orElseThrow();
        var userEntity = userRepository.findById(signedInUser.getId()).orElseThrow(() -> new RuntimeException("cant find user!"));
        return userMapper.toUserResponse(userEntity);
    }
    @Override
    public JwtResponse login(@RequestBody LoginRequest loginRequest) {
        var userEntity = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User not found."));
        var authentication = authenticateUser(loginRequest);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
        return new JwtResponse(jwt, userEntity.getId() ,userEntity.getUsername(), userEntity.getEmail());
    }

    @Override
    public UserResponse registerUser(SignupRequest signupRequest) {
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
         throw  new RuntimeException("Username already taken");
        }
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw  new RuntimeException("Email already taken");
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
        UserEntity result = userRepository.save(user);
        return userMapper.toUserResponse(result);
    }

    private Authentication authenticateUser(final LoginRequest loginRequest) {
        try {
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new RuntimeException("Unauthorization ! ");
        }
    }

}
