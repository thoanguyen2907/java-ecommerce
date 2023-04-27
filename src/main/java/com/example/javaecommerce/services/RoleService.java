package com.example.javaecommerce.services;

import com.example.javaecommerce.model.request.RoleRequest;
import com.example.javaecommerce.model.response.RoleResponse;

import java.util.List;

public interface RoleService {
    List<RoleResponse> getAllRoles();

    RoleResponse addRole(RoleRequest role);

    RoleResponse getRoleById(Long roleId);

    void deleteRole(Long roleId) throws Exception;

    RoleResponse updateRole(RoleRequest roleRequest, Long id);

    RoleResponse addRoleForUser(Long userId, RoleRequest roleRequest);
}
