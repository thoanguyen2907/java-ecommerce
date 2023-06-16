package com.example.javaecommerce.controller;

import com.example.javaecommerce.model.request.RoleRequest;
import com.example.javaecommerce.model.response.RoleResponse;
import com.example.javaecommerce.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/roles")
@RequiredArgsConstructor
public class RoleController {
    private RoleService roleService;

    @GetMapping
    public ResponseEntity<?> getAllRoles() {
        List<RoleResponse> roleResponses = roleService.getAllRoles();
        return ResponseEntity.ok(roleResponses);
    }

    @PostMapping
    public RoleResponse addRole(@RequestBody final RoleRequest roleRequest) {
        return roleService.addRole(roleRequest);
    }

    @PostMapping("/users/{userId}/")
    public RoleResponse addRoleForUser(@PathVariable(value = "userId") final Long userId,
                                       final RoleRequest roleRequest) {
        RoleResponse role = roleService.addRoleForUser(userId, roleRequest);
        return role;
    }

    @DeleteMapping(path = "{roleId}")
    public void deleteRole(@PathVariable("roleId") final Long roleId) throws Exception {
        roleService.deleteRole(roleId);
    }

    @PutMapping
    public RoleResponse updateRole(@RequestBody final RoleRequest roleRequest, final Long id) {
        return roleService.updateRole(roleRequest, id);
    }
}
