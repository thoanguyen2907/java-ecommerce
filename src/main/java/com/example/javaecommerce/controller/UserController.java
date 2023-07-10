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
    public UserResponse addUser(@RequestBody final UserRequest userRequest) {
        return userService.addUser(userRequest);
    }

    @DeleteMapping(path = "{userId}")
    public void deleteUser(@PathVariable("userId") final Long userId) {
        userService.deleteUser(userId);
    }

    @PutMapping
    public UserResponse updateUser(@RequestBody final UserRequest userRequest, final Long id) {
        return userService.updateUser(userRequest, id);
    }
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        UserResponse userProfile = userService.aboutMe();
        return ResponseEntity.ok(userProfile);
    }
}
