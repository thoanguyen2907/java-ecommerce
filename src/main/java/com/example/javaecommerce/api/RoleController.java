package com.example.javaecommerce.api;

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
    public RoleResponse addRole(@RequestBody RoleRequest roleRequest) {
        return roleService.addRole(roleRequest);
    }

    @PostMapping("/users/{userId}/")
    public RoleResponse addRoleForUser(@PathVariable(value = "userId") Long userId,
                                       RoleRequest roleRequest) {
        RoleResponse role = roleService.addRoleForUser(userId, roleRequest);
        return role;
    }

    @DeleteMapping(path = "{roleId}")
    public void deleteRole(@PathVariable("roleId") Long roleId) throws Exception {
        roleService.deleteRole(roleId);
    }

    @PutMapping
    public RoleResponse updateRole(@RequestBody RoleRequest roleRequest, Long id) {
        return roleService.updateRole(roleRequest, id);
    }
}
