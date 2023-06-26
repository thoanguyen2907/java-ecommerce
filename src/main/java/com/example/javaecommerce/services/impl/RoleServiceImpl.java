package com.example.javaecommerce.services.impl;

import com.example.javaecommerce.converter.Converter;
import com.example.javaecommerce.exception.ResourceNotFoundException;
import com.example.javaecommerce.model.ERole;
import com.example.javaecommerce.model.entity.*;
import com.example.javaecommerce.model.request.RoleRequest;
import com.example.javaecommerce.model.response.RoleResponse;
import com.example.javaecommerce.repository.RoleRepository;
import com.example.javaecommerce.repository.UserRepository;
import com.example.javaecommerce.services.RoleService;


import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    private final UserRepository userRepository;

    @Override
    public List<RoleResponse> getAllRoles() {
        List<RoleEntity> roleEntities = roleRepository.findAll();
        return Converter.toList(roleEntities, RoleResponse.class);
    }

    @Override
    public RoleResponse addRole(final RoleRequest role) {
        RoleEntity roleEntity = Converter.toModel(role, RoleEntity.class);
        roleRepository.save(roleEntity);
        return Converter.toModel(roleEntity, RoleResponse.class);
    }

    @Override
    public RoleResponse getRoleById(final Long roleId) {
        RoleEntity roleEntity = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "id", roleId)
                );
        return Converter.toModel(roleEntity, RoleResponse.class);
    }

    @Override
    public void deleteRole(final Long roleId) {
        roleRepository.deleteById(roleId);
    }

    @Override
    public RoleResponse updateRole(final RoleRequest roleRequest, final Long id) {
        return null;
    }

    @Override
    public RoleResponse addRoleForUser(final Long userId, final RoleRequest roleRequest) {
        RoleEntity _role = userRepository.findById(userId).map(user -> {
            String roleName = roleRequest.getName();
            RoleEntity role = roleRepository.findByName(ERole.valueOf(roleName)).orElseThrow(() -> new RuntimeException("cant find role"));
            userRepository.save(user);
            return role;
        }).orElseThrow(() -> new RuntimeException("cant find user"));
        return Converter.toModel(_role, RoleResponse.class);
    }
}
