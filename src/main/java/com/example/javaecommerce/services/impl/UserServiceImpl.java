package com.example.javaecommerce.services.impl;

import com.example.javaecommerce.event.ForgotPasswordCompleteEvent;
import com.example.javaecommerce.event.RegistrationCompleteEvent;
import com.example.javaecommerce.exception.EcommerceRunTimeException;
import com.example.javaecommerce.exception.ErrorCode;
import com.example.javaecommerce.mapper.UserMapper;
import com.example.javaecommerce.model.ERole;
import com.example.javaecommerce.model.entity.PasswordResetToken;
import com.example.javaecommerce.model.entity.RoleEntity;
import com.example.javaecommerce.model.entity.UserEntity;
import com.example.javaecommerce.model.entity.VerificationToken;
import com.example.javaecommerce.model.request.*;
import com.example.javaecommerce.model.response.JwtResponse;

import com.example.javaecommerce.model.response.UserResponse;
import com.example.javaecommerce.pagination.OffsetPageRequest;
import com.example.javaecommerce.pagination.PaginationPage;
import com.example.javaecommerce.repository.PasswordResetTokenRepository;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final ApplicationEventPublisher publisher;
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public List<UserResponse> getAllUsers() {
        try {
            List<UserEntity> userEntities = userRepository.findAll();
            logger.info("Get list of users successfully");
            return userMapper.toListUserResponse(userEntities);
        } catch (Exception e) {
            logger.info("Failed to get list of users ", e);
            throw new RuntimeException("Failed to get list of users ");
        }
    }

    @Override
    public UserResponse addUser(final UserRequest userRequest) {
        try {
            if (userRepository.existsByEmail(userRequest.getEmail())) {
                logger.info("Email is already existed");
                throw new EcommerceRunTimeException(ErrorCode.ALREADY_EXIST);
            }
            UserEntity user = new UserEntity();
            user.setEmail(userRequest.getEmail());

            Set<RoleEntity> roles = new HashSet<>();
            RoleEntity userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new EcommerceRunTimeException(ErrorCode.ID_NOT_FOUND));
            logger.info("Role is found {}", userRole);
            roles.add(userRole);

            UserEntity result = userRepository.save(user);
            logger.info("Create user successfully");
            return userMapper.toUserResponse(result);
        } catch (Exception e) {
            logger.info("Failed to get list of users ", e);
            throw new RuntimeException("Failed to get list of users");
        }
    }

    @Override
    public void deleteUser(final Long userId) {
        try {
            UserEntity user = userRepository
                    .findById(userId)
                    .orElseThrow(() -> new EcommerceRunTimeException(ErrorCode.ID_NOT_FOUND));
            logger.info("User is found by id {} ", userId);
            userRepository.deleteById(user.getId());
            logger.info("Delete user successfully !");
        } catch (Exception e) {
            logger.info("Failed to delete user by id ", e);
            throw new RuntimeException("Failed to delete user by id");
        }
    }

    @Override
    public UserResponse updateUser(final UserRequest userRequest, final Long id) {
        try {
            UserEntity userEntity = userRepository.findById(id)
                    .map(user -> {
                        user.setEmail(userRequest.getEmail());
                        user.setPassword(userRequest.getPassword());
                        return userRepository.save(user);
                    })
                    .orElseThrow(() -> new EcommerceRunTimeException(ErrorCode.ID_NOT_FOUND));
            logger.info("Update user successfully by id : ", userEntity.getId());
            return userMapper.toUserResponse(userEntity);
        } catch (Exception e) {
            logger.info("Failed to update user by id ", e);
            throw new RuntimeException("Failed to update user by id");
        }
    }

    @Override
    public PaginationPage<UserResponse> getUserByPagination(final Integer offset, final Integer limited) {
        try {
            var pageable = new OffsetPageRequest(offset, limited);
            var userList = userRepository.findAll(pageable);
            logger.info("Get list of users by find all (pageable)");
            var userResponse = userList
                    .getContent()
                    .stream()
                    .map(item -> userMapper.toUserResponse(item))
                    .collect(Collectors.toList());
            logger.info("Get list of users successfully by pagination");
            return new PaginationPage<UserResponse>()
                    .setOffset(userList.getNumber())
                    .setTotalRecords(userList.getTotalElements())
                    .setRecords(userResponse)
                    .setLimit(userList.getSize());
        } catch (Exception e) {
            logger.info("Failed to get list of users user by id ", e);
            throw new RuntimeException("Failed to get list of users");
        }
    }

    @Override
    public UserResponse aboutMe() {
        var signedInUser = JWTSecurity.getJWTUserInfo().orElseThrow();
        var userEntity = userRepository.findById(signedInUser.getId())
                .orElseThrow(() -> new EcommerceRunTimeException(ErrorCode.ID_NOT_FOUND));
        return userMapper.toUserResponse(userEntity);
    }

    @Override
    public void saveVerificationTokenForUser(final String token, final UserEntity user) {
        try {
            VerificationToken verificationToken = new VerificationToken(user, token);
            verificationTokenRepository.save(verificationToken);
            logger.info("Save verification successfully !!");
        } catch (Exception e) {
            logger.info("Failed to save the verification token ", e);
            throw new RuntimeException("Failed to save the verification token");
        }
    }

    @Override
    public String validateVerificationToken(final String token) {
        try {
            VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
            if (verificationToken == null) {
                logger.info("Cannot find verification token !");
                return "invalid";
            }
            UserEntity user = verificationToken.getUser();
            Calendar calendar = Calendar.getInstance();
            if ((verificationToken.getExpirationTime().getTime() - calendar.getTime().getTime()) <= 0) {
                verificationTokenRepository.delete(verificationToken);
                logger.info("Verification token expired");
                return "expired";
            }
            user.setEnabled(true);
            logger.info("Set user enable to true !");
            userRepository.save(user);
            logger.info("Save user successfully !");
            return "valid";
        } catch (Exception e) {
            logger.info("Failed to validate token ", e);
            throw new RuntimeException("Failed to validate token");
        }
    }

    @Override
    public void createPasswordResetTokenForUser(final UserEntity user, final String token, final String urlLink) {
        try {
            // save password reset token in db, and trigger event forgot to send email with user, url, token
            PasswordResetToken passwordResetToken = new PasswordResetToken(user, token);
            passwordResetTokenRepository.save(passwordResetToken);
            logger.info("Create password reset token successfully !");
            //push the event to send email forgot password to user
            publisher.publishEvent(new ForgotPasswordCompleteEvent(
                    user,
                    urlLink,
                    token));
            logger.info("trigger  event to send email ");
        } catch (Exception e) {
            logger.info("Failed to create password reset token ", e);
            throw new RuntimeException("Failed to create password reset token");
        }
    }

    @Override
    public void checkAndCreatePasswordResetTokenForUser(final ResetEmail resetEmail, final HttpServletRequest request) {
        try {
            // user send email to reset the password, check user email existed or not
            UserEntity user = userRepository.findByEmail(resetEmail.getEmail())
                    .orElseThrow(() -> new EcommerceRunTimeException(ErrorCode.ID_NOT_FOUND));
            // user email existed
            if (user != null) {
                logger.info("User is existed !");
                // create token and url link
                String token = UUID.randomUUID().toString();
                logger.info("Generate token");
                var applicationUrl = JWTSecurity.applicationUrl(request);
                //send link url and password token to user email
                String urlLink = applicationUrl + "/api/auth/savePassword?token=" + token;
                logger.info("Create url");
                // handle in method createPasswordResetTokenForUser with user, token , url
                createPasswordResetTokenForUser(user, token, urlLink);
                logger.info("Create password reset token, generate url successfully");
            }
        } catch (Exception e) {
            logger.info("Failed to create password reset token ", e);
            throw new RuntimeException("Failed to create password reset token");
        }
    }

    @Override
    public void resetPassword(final String token, final PasswordResetModel passwordResetModel) {
        // compare password token existed in db or not
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);
        logger.info("Password reset token is found");
        // if password token is not existed, throw invalid token
        if (passwordResetToken == null) {
            logger.info("Cannot find password reset token");
            throw new EcommerceRunTimeException(ErrorCode.INVALID_TOKEN);
        }
        // if password token existed, get user
        UserEntity user = passwordResetToken.getUser();
        Calendar calendar = Calendar.getInstance();
        // compare the time -> expired or not
        if ((passwordResetToken.getExpirationTime().getTime() - calendar.getTime().getTime()) <= 0) {
            logger.info("Password reset token is expired");
            passwordResetTokenRepository.delete(passwordResetToken);
            logger.info("Delete password reset token when expired");
            throw new EcommerceRunTimeException(ErrorCode.EXPIRED_TOKEN);
        }
        // if valid token and time, set new password and save
        user.setPassword(passwordEncoder.encode(passwordResetModel.getNewPassword()));
        userRepository.save(user);
        logger.info("Set and save new password successfully !");
    }

    @Override
    public JwtResponse login(@RequestBody final LoginRequest loginRequest) {
        var userEntity = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new EcommerceRunTimeException(ErrorCode.ID_NOT_FOUND));
        var authentication = authenticateUser(loginRequest);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        logger.info("Set authentication");
        String jwt = jwtUtils.generateJwtToken(authentication);
        logger.info("Generate token successfully ");
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        logger.info("Get user details");
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
        return new JwtResponse(jwt, userEntity.getId(), userEntity.getEmail(), roles);
    }

    @Override
    @Transactional
    public UserResponse registerUser(final SignupRequest signupRequest, final HttpServletRequest httpServletRequest) {
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            logger.info("User email is existed already");
            throw new EcommerceRunTimeException(ErrorCode.ALREADY_EXIST);
        }
        UserEntity user = new UserEntity(
                signupRequest.getEmail(),
                passwordEncoder.encode(signupRequest.getPassword()));
        logger.info("Create user by email and hash password");
        Set<RoleEntity> roles = new HashSet<>();
        RoleEntity userRole = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new EcommerceRunTimeException(ErrorCode.ID_NOT_FOUND));
        roles.add(userRole);
        user.setRoles(roles);
        logger.info("Set role as USER");
        UserEntity result = userRepository.save(user);
        logger.info("Save user to database");
        //after create new user, create and save verified token and send email
        String token = String.valueOf(UUID.randomUUID());
        logger.info("Create token to verify email register");
        saveVerificationTokenForUser(token, user);
        logger.info("Save verified token to database");
        publisher.publishEvent(new RegistrationCompleteEvent(
                result,
                JWTSecurity.applicationUrl(httpServletRequest),
                token));
        logger.info("Trigger event to send email for verification");
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
