package com.example.javaecommerce.services;

import com.example.javaecommerce.model.entity.UserEntity;
import com.example.javaecommerce.model.request.LoginRequest;
import com.example.javaecommerce.model.request.PasswordResetModel;
import com.example.javaecommerce.model.request.SignupRequest;
import com.example.javaecommerce.model.request.UserRequest;
import com.example.javaecommerce.model.response.JwtResponse;
import com.example.javaecommerce.model.response.UserResponse;
import com.example.javaecommerce.pagination.PaginationPage;


import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface UserService {
    List<UserResponse> getAllUsers();

    JwtResponse login(LoginRequest loginFormRequest);

    UserResponse registerUser(SignupRequest signupRequest, HttpServletRequest httpServletRequest);

    UserResponse addUser(UserRequest userRequest);

    void deleteUser(Long userID);

    UserResponse updateUser(UserRequest userRequest, Long id);

    PaginationPage<UserResponse> getUserByPagination(Integer offset, Integer limited);

    UserResponse aboutMe();

    void saveVerificationTokenForUser(String token, UserEntity user);

    String validateVerificationToken(String token);

    UserEntity findUserByEmail(String email);

    void createPasswordResetTokenForUser(UserEntity user, String token, String urlLink);

    String validatePasswordResetToken(String token);

    UserEntity getUserByPasswordResetToken(String token);

    void saveResetPassword(UserEntity user, PasswordResetModel passwordResetModel);
}
