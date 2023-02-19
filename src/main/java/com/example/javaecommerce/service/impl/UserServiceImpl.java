package com.example.javaecommerce.service.impl;

import com.example.javaecommerce.converter.Converter;
import com.example.javaecommerce.model.ERole;
import com.example.javaecommerce.model.entity.RoleEntity;
import com.example.javaecommerce.model.entity.UserEntity;
import com.example.javaecommerce.model.request.UserRequest;
import com.example.javaecommerce.model.response.UserResponse;
import com.example.javaecommerce.repository.RoleRepository;
import com.example.javaecommerce.repository.UserRepository;
import com.example.javaecommerce.service.UserService;

import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
@Transactional

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public List<UserResponse> getAllUsers() {
        List<UserEntity> userEntities = (List<UserEntity>) userRepository.findAll();
        return Converter.toList(userEntities, UserResponse.class);
    }
    @Override
    public Map<String, Object> getUserByPagination(String username, int page, int size) {
        List<UserEntity> userEntities = new ArrayList<UserEntity>();
        Pageable paging = PageRequest.of(page, size);
        Page<UserEntity> pageUsers;
        if(username == null) pageUsers = userRepository.findAll(paging);
        else
            pageUsers = userRepository.findByUsername(username, paging);
        userEntities = pageUsers.getContent();
        List<UserResponse>  userResponses=  Converter.toList(userEntities, UserResponse.class);
        Map<String, Object> response = new HashMap<>();
        response.put("users", userResponses);
        response.put("currentPage", pageUsers.getNumber());
        response.put("totalItems", pageUsers.getTotalElements());
        response.put("totalPages", pageUsers.getTotalPages());
        return   response;
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
                            .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not found."));
                    roles.add(adminRole);
                    break;
                case "moderator":
                    RoleEntity therapistRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                            .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not found."));
                    roles.add(therapistRole);
                    break;
                default:
                    RoleEntity userRole = roleRepository.findByName(ERole.ROLE_USER)
                            .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not found."));
                    roles.add(userRole);
            }
        });
        user.setRoles(roles);
        UserEntity result = userRepository.save(user);
        return Converter.toModel(result, UserResponse.class);
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
                }).orElseThrow(() -> new IllegalStateException(
                        "user with id " + id + " does not exist"
                ));
        return Converter.toModel(userEntity, UserResponse.class);
    }



}
