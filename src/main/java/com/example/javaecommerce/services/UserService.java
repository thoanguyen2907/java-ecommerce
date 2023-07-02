package com.example.javaecommerce.services;

import com.example.javaecommerce.model.entity.UserEntity;
import com.example.javaecommerce.model.request.*;
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

    void createPasswordResetTokenForUser(UserEntity user, String token, String urlLink);

    void checkAndCreatePasswordResetTokenForUser(ResetEmail resetEmail, HttpServletRequest request);

    void saveResetPassword(UserEntity user, PasswordResetModel passwordResetModel);

    void resetPassword(String token, PasswordResetModel passwordResetModel);
}
