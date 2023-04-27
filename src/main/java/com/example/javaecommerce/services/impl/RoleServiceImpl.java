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

import javax.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<RoleResponse> getAllRoles() {
        List<RoleEntity> roleEntities = roleRepository.findAll();
        return Converter.toList(roleEntities, RoleResponse.class);
    }

    @Override
    public RoleResponse addRole(RoleRequest role) {
        RoleEntity roleEntity = Converter.toModel(role, RoleEntity.class);
        roleRepository.save(roleEntity);
        return Converter.toModel(roleEntity, RoleResponse.class);
    }

    @Override
    public RoleResponse getRoleById(Long roleId) {
        RoleEntity roleEntity = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "id", roleId)
                );
        return Converter.toModel(roleEntity, RoleResponse.class);
    }

    @Override
    public void deleteRole(Long roleId) throws Exception {
        roleRepository.deleteById(roleId);
    }

    @Override
    public RoleResponse updateRole(RoleRequest roleRequest, Long id) {
        return null;
    }

    @Override
    public RoleResponse addRoleForUser(Long userId, RoleRequest roleRequest) {
        RoleEntity _role = userRepository.findById(userId).map(user -> {
            String roleName = roleRequest.getName();
            RoleEntity role = roleRepository.findByName(ERole.valueOf(roleName)).orElseThrow(() -> new RuntimeException("cant find role"));
            //  user.addRole(role);
            userRepository.save(user);
            return role;
        }).orElseThrow(() -> new RuntimeException("cant find user"));
        return Converter.toModel(_role, RoleResponse.class);
    }
}
