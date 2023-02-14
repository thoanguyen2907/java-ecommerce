package com.example.javaecommerce.api;

import com.example.javaecommerce.model.request.UserRequest;
import com.example.javaecommerce.model.response.UserResponse;
import com.example.javaecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        List<UserResponse>  userResponse = userService.getAllUsers();
        return ResponseEntity.ok(userResponse);
    }

/*    @GetMapping(path = "/pagination/{pageNum}")
    public Page<UserEntity> getAllUsersByPagination(@PathVariable("pageNum") int pageNum, @Param("sortField") String sortField,
                                                    @Param("keyword") String keyword ) {
        Page<UserEntity> usersPagination = userService.getAllUsersByPagination(pageNum, sortField, keyword);
        return  usersPagination;
    }*/

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
/*    @GetMapping(path = "page/{pageNum}/{userPerPage}")
    public String listByPage (@PathVariable(name =  "pageNum") int pageNum, @PathVariable(name =  "userPerPage") int userPerPage) {
        Page<UserEntity> page = userService.listByPage(pageNum, userPerPage);

        List<UserEntity> listUsers = page.getContent();
        long startCount = (pageNum -1) * userPerPage + 1;
        long endCount = startCount + userPerPage -1;
        if(endCount > page.getTotalElements()) {
            endCount = page.getTotalElements();
        }

        System.out.println("Pagenum =  " + page.getTotalElements());
        System.out.println("Pagenum =  " + page.getTotalPages());
        return "users";
    }*/

}
