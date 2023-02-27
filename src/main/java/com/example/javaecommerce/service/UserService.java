package com.example.javaecommerce.service;
import com.example.javaecommerce.model.request.UserRequest;
import com.example.javaecommerce.model.response.UserResponse;


import java.util.List;
import java.util.Map;

public interface UserService {
    List<UserResponse> getAllUsers();

    UserResponse addUser(UserRequest userRequest);
    void deleteUser(Long userID);

    UserResponse updateUser(UserRequest userRequest, Long id);

    Map<String, Object> getUserByPagination (String username, int page, int size);
}
