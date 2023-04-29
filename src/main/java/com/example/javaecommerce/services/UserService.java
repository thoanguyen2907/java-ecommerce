package com.example.javaecommerce.services;

import com.example.javaecommerce.model.request.LoginRequest;
import com.example.javaecommerce.model.request.UserRequest;
import com.example.javaecommerce.model.response.JwtResponse;
import com.example.javaecommerce.model.response.UserResponse;
import com.example.javaecommerce.pagination.PaginationPage;


import java.util.List;

public interface UserService {
    List<UserResponse> getAllUsers();
    JwtResponse login(LoginRequest loginFormRequest);
    UserResponse addUser(UserRequest userRequest);

    void deleteUser(Long userID);

    UserResponse updateUser(UserRequest userRequest, Long id);

    PaginationPage<UserResponse> getUserByPagination(Integer offset, Integer limited);

    UserResponse aboutMe();
}
