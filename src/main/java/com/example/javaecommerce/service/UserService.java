package com.example.javaecommerce.service;


import com.example.javaecommerce.model.entity.UserEntity;
import com.example.javaecommerce.model.request.UserRequest;
import com.example.javaecommerce.model.response.UserResponse;


import java.util.List;

public interface UserService {
    List<UserResponse> getAllUsers();

    UserResponse addUser(UserRequest userRequest);
    void deleteUser(Long userID);

    UserResponse updateUser(UserRequest userRequest, Long id);
    List<UserEntity> getAllUsersByPagination(int pageNo, int pageSize);
}
