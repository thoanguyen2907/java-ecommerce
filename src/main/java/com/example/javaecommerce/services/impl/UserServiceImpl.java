package com.example.javaecommerce.services.impl;

import com.example.javaecommerce.exception.ResourceNotFoundException;
import com.example.javaecommerce.mapper.UserMapper;
import com.example.javaecommerce.model.ERole;
import com.example.javaecommerce.model.entity.RoleEntity;
import com.example.javaecommerce.model.entity.UserEntity;
import com.example.javaecommerce.model.request.UserRequest;
import com.example.javaecommerce.model.response.UserResponse;
import com.example.javaecommerce.pagination.OffsetPageRequest;
import com.example.javaecommerce.pagination.PaginationPage;
import com.example.javaecommerce.repository.RoleRepository;
import com.example.javaecommerce.repository.UserRepository;
import com.example.javaecommerce.services.UserService;

import javax.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

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


}
