package com.example.javaecommerce.services.impl;

import com.example.javaecommerce.converter.Converter;
import com.example.javaecommerce.exception.EcommerceRunTimeException;
import com.example.javaecommerce.exception.ErrorCode;

import com.example.javaecommerce.model.ERole;
import com.example.javaecommerce.model.entity.*;
import com.example.javaecommerce.model.request.RoleRequest;
import com.example.javaecommerce.model.response.RoleResponse;
import com.example.javaecommerce.repository.RoleRepository;
import com.example.javaecommerce.repository.UserRepository;
import com.example.javaecommerce.services.RoleService;


import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);

    @Override
    public List<RoleResponse> getAllRoles() {
        try {
            List<RoleEntity> roleEntities = roleRepository.findAll();
            logger.info("Get all roles successfully !");
            return Converter.toList(roleEntities, RoleResponse.class);
        } catch (Exception e) {
            logger.info("Failed to get all roles", e);
            throw new RuntimeException("Failed to get all roles");
        }
    }

    @Override
    public RoleResponse addRole(final RoleRequest role) {
        try {
            RoleEntity roleEntity = Converter.toModel(role, RoleEntity.class);
            roleRepository.save(roleEntity);
            logger.info("Create role successfully ! ");
            return Converter.toModel(roleEntity, RoleResponse.class);
        } catch (Exception e) {
            logger.info("Failed to create role", e);
            throw new RuntimeException("Failed to create role");
        }
    }

    @Override
    public RoleResponse getRoleById(final Long roleId) {
        try {
            RoleEntity roleEntity = roleRepository.findById(roleId)
                    .orElseThrow(() -> new EcommerceRunTimeException(ErrorCode.ID_NOT_FOUND)
                    );
            logger.info("Get role by id successfully {} ", roleId);
            return Converter.toModel(roleEntity, RoleResponse.class);
        } catch (Exception e) {
            logger.info("Failed to get role by id ", e);
            throw new RuntimeException("Failed to get role by id");
        }
    }

    @Override
    public void deleteRole(final Long roleId) {
        try {
            RoleEntity role = roleRepository
                    .findById(roleId)
                    .orElseThrow(() -> new EcommerceRunTimeException(ErrorCode.ID_NOT_FOUND));
            logger.info("Cannot find role by id {} ", roleId);
            if (role != null) {
                roleRepository.deleteById(roleId);
                logger.info("Delete role by id successfully {} ", roleId);
            }
        } catch (Exception e) {
            logger.info("Failed to get role by id ", e);
            throw new RuntimeException("Failed to get role by id");
        }
    }

    @Override
    public RoleResponse updateRole(final RoleRequest roleRequest, final Long id) {
        return null;
    }

    @Override
    public RoleResponse addRoleForUser(final Long userId, final RoleRequest roleRequest) {
        try {
            RoleEntity _role = userRepository.findById(userId).map(user -> {
                String roleName = roleRequest.getName();
                RoleEntity role = roleRepository.findByName(ERole.valueOf(roleName))
                        .orElseThrow(() -> new EcommerceRunTimeException(ErrorCode.ID_NOT_FOUND));
                // get list of roles from user
                var roles = user.getRoles();
                // add role to role list
                roles.add(role);
                // save user after adding role
                userRepository.save(user);
                return role;
            }).orElseThrow(() -> new EcommerceRunTimeException(ErrorCode.ID_NOT_FOUND));
            logger.info("Add role for user successfully");
            return Converter.toModel(_role, RoleResponse.class);
        } catch (Exception e) {
            logger.info("Failed to get role by id ", e);
            throw new RuntimeException("Failed to get role by id");
        }
    }
}
