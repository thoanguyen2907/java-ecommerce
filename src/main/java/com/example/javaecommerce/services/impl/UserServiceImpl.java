package com.example.javaecommerce.services.impl;

import com.example.javaecommerce.event.RegistrationCompleteEvent;
import com.example.javaecommerce.exception.EcommerceRunTimeException;
import com.example.javaecommerce.exception.ErrorCode;
import com.example.javaecommerce.mapper.UserMapper;
import com.example.javaecommerce.model.ERole;
import com.example.javaecommerce.model.entity.RoleEntity;
import com.example.javaecommerce.model.entity.UserEntity;
import com.example.javaecommerce.model.entity.VerificationToken;
import com.example.javaecommerce.model.request.LoginRequest;
import com.example.javaecommerce.model.request.SignupRequest;
import com.example.javaecommerce.model.request.UserRequest;
import com.example.javaecommerce.model.response.JwtResponse;

import com.example.javaecommerce.model.response.UserResponse;
import com.example.javaecommerce.pagination.OffsetPageRequest;
import com.example.javaecommerce.pagination.PaginationPage;
import com.example.javaecommerce.repository.RoleRepository;
import com.example.javaecommerce.repository.UserRepository;
import com.example.javaecommerce.repository.VerificationTokenRepository;
import com.example.javaecommerce.security.jwt.JwtUtils;
import com.example.javaecommerce.security.services.UserDetailsImpl;
import com.example.javaecommerce.services.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import com.example.javaecommerce.utils.JWTSecurity;
import lombok.RequiredArgsConstructor;

import org.springframework.context.ApplicationEventPublisher;
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
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final ApplicationEventPublisher publisher;

    @Override
    public List<UserResponse> getAllUsers() {
        List<UserEntity> userEntities = userRepository.findAll();
        return userMapper.toListUserResponse(userEntities);
    }

    @Override
    public UserResponse addUser(final UserRequest userRequest) {
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new EcommerceRunTimeException(ErrorCode.ALREADY_EXIST);
        }
        UserEntity user = new UserEntity();
        user.setEmail(userRequest.getEmail());

        Set<RoleEntity> roles = new HashSet<>();
        RoleEntity userRole = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new EcommerceRunTimeException(ErrorCode.ID_NOT_FOUND));
        roles.add(userRole);

        UserEntity result = userRepository.save(user);
        return userMapper.toUserResponse(result);
    }

    @Override
    public void deleteUser(final Long userID) {
        userRepository.deleteById(userID);
    }

    @Override
    public UserResponse updateUser(final UserRequest userRequest, final Long id) {
        UserEntity userEntity = userRepository.findById(id)
                .map(user -> {
                    user.setEmail(userRequest.getEmail());
                    user.setPassword(userRequest.getPassword());
                    return userRepository.save(user);
                }).orElseThrow(() -> new EcommerceRunTimeException(ErrorCode.ID_NOT_FOUND));
        return userMapper.toUserResponse(userEntity);
    }

    @Override
    public PaginationPage<UserResponse> getUserByPagination(final Integer offset, final Integer limited) {
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
        var userEntity = userRepository.findById(signedInUser.getId())
                .orElseThrow(() -> new EcommerceRunTimeException(ErrorCode.ID_NOT_FOUND));
        return userMapper.toUserResponse(userEntity);
    }

    @Override
    public void saveVerificationTokenForUser(String token, UserEntity user) {
        VerificationToken verificationToken = new VerificationToken(user, token);
        verificationTokenRepository.save(verificationToken);
    }

    @Override
    public String validateVerificationToken(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
        if (verificationToken == null) {
            return "invalid";
        }
        UserEntity user = verificationToken.getUser();
        Calendar calendar = Calendar.getInstance();
        if ((verificationToken.getExpirationTime().getTime() - calendar.getTime().getTime()) <= 0) {
            verificationTokenRepository.delete(verificationToken);
            return "expired";
        }
        user.setEnabled(true);
        userRepository.save(user);
        return "valid";
    }

    @Override
    public JwtResponse login(@RequestBody final LoginRequest loginRequest) {
        var userEntity = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new EcommerceRunTimeException(ErrorCode.ID_NOT_FOUND));
        var authentication = authenticateUser(loginRequest);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
        return new JwtResponse(jwt, userEntity.getId(), userEntity.getUsername(), userEntity.getEmail(), roles);
    }

    @Override
    @Transactional
    public UserResponse registerUser(final SignupRequest signupRequest, final HttpServletRequest httpServletRequest) {
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            throw new EcommerceRunTimeException(ErrorCode.ALREADY_EXIST);
        }
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new EcommerceRunTimeException(ErrorCode.ALREADY_EXIST);
        }
        UserEntity user = new UserEntity(signupRequest.getUsername(),
                signupRequest.getEmail(),
                passwordEncoder.encode(signupRequest.getPassword()));
        Set<RoleEntity> roles = new HashSet<>();
        RoleEntity userRole = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new EcommerceRunTimeException(ErrorCode.ID_NOT_FOUND));
        roles.add(userRole);
        user.setRoles(roles);
        UserEntity result = userRepository.save(user);
        publisher.publishEvent(new RegistrationCompleteEvent(
                result,
                JWTSecurity.applicationUrl(httpServletRequest)));
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
            throw new EcommerceRunTimeException(ErrorCode.UNAUTHENTICATED);
        }
    }

}
