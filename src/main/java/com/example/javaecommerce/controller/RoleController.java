package com.example.javaecommerce.controller;

import com.example.javaecommerce.model.request.RoleRequest;
import com.example.javaecommerce.model.response.RoleResponse;
import com.example.javaecommerce.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/roles")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @GetMapping
    public ResponseEntity<?> getAllRoles() {
        List<RoleResponse> roleResponses = roleService.getAllRoles();
        return ResponseEntity.ok(roleResponses);
    }

    @PostMapping
    public ResponseEntity<RoleResponse> addRole(@RequestBody final RoleRequest roleRequest) {
        var roleResponse =  roleService.addRole(roleRequest);
        return ResponseEntity.ok(roleResponse);
    }

    @PostMapping("/users/{userId}/")
    public ResponseEntity<RoleResponse> addRoleForUser(@PathVariable(value = "userId") final Long userId,
                                       final RoleRequest roleRequest) {
        var roleResponse = roleService.addRoleForUser(userId, roleRequest);
        return ResponseEntity.ok(roleResponse);
    }

    @DeleteMapping(path = "{roleId}")
    public ResponseEntity<?> deleteRole(@PathVariable("roleId") final Long roleId) throws Exception {
        roleService.deleteRole(roleId);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<RoleResponse> updateRole(@RequestBody final RoleRequest roleRequest, final Long id) {
        var roleResponse = roleService.updateRole(roleRequest, id);
        return ResponseEntity.ok(roleResponse);
    }
}
