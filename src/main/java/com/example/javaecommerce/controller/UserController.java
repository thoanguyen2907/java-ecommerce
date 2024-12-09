package com.example.javaecommerce.controller;

import com.example.javaecommerce.model.request.UserRequest;
import com.example.javaecommerce.model.response.UserResponse;
import com.example.javaecommerce.pagination.PaginationPage;
import com.example.javaecommerce.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        List<UserResponse> userResponse = userService.getAllUsers();
        return ResponseEntity.ok(userResponse);
    }

    @GetMapping(path = "/pagination")
    public ResponseEntity<?> listAllUsersByPagination(@RequestParam(name = "offset", defaultValue = "1") final Integer offset,
                                                      @RequestParam(name = "limit", defaultValue = "10") final Integer limit) {
        PaginationPage<UserResponse> userResponses = userService.getUserByPagination(offset, limit);
        return ResponseEntity.ok(userResponses);
    }

    @PostMapping
    public ResponseEntity<UserResponse> addUser(@RequestBody final UserRequest userRequest) {
        var userResponse = userService.addUser(userRequest);
        return ResponseEntity.ok(userResponse);
    }

    @DeleteMapping(path = "{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable("userId") final Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<UserResponse> updateUser(@RequestBody final UserRequest userRequest, final Long id) {
        var userResponse = userService.updateUser(userRequest, id);
        return ResponseEntity.ok(userResponse);
    }
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        UserResponse userProfile = userService.aboutMe();
        return ResponseEntity.ok(userProfile);
    }
}
