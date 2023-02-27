package com.example.javaecommerce.api;

import com.example.javaecommerce.model.entity.UserEntity;
import com.example.javaecommerce.model.request.UserRequest;
import com.example.javaecommerce.model.response.UserResponse;
import com.example.javaecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        List<UserResponse>  userResponse = userService.getAllUsers();
        return ResponseEntity.ok(userResponse);
    }
    @GetMapping(path = "/pagination")
    public ResponseEntity<?> listAllUsersByPagination( @RequestParam(required = false) String username,
                                                       @RequestParam(defaultValue = "0") Integer page,
                                                       @RequestParam(defaultValue = "3") Integer size ){
        Map<String, Object> userResponses = userService.getUserByPagination(username, page, size);
        return ResponseEntity.ok(userResponses);
    }
    @PostMapping
    public UserResponse addUser(@RequestBody UserRequest userRequest) {
        return userService.addUser(userRequest);
    }
    @DeleteMapping(path = "{userId}")
    public void deleteUser(@PathVariable("userId") Long userId) {
        userService.deleteUser(userId);
    }
    @PutMapping
    public UserResponse updateUser(@RequestBody UserRequest userRequest, Long id) {
        return userService.updateUser(userRequest, id);
    }
}
