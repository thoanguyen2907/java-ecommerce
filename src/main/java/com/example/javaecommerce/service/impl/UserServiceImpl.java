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

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
@Transactional

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    public static final int USERS_PER_PAGE = 4;


    @Override
    public List<UserResponse> getAllUsers() {
        List<UserEntity> userEntities = (List<UserEntity>) userRepository.findAll();
        return Converter.toList(userEntities, UserResponse.class);
    }


    public List<RoleEntity> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public UserResponse addUser(UserRequest userRequest) {
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new IllegalStateException("Email is already existed !!");
        }
        UserEntity user = new UserEntity();
        user.setEmail(userRequest.getEmail());
        Set<String> strRoles = userRequest.getRole();
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
