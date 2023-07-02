package com.example.javaecommerce.register;

import com.example.javaecommerce.model.ERole;
import com.example.javaecommerce.model.entity.RoleEntity;
import com.example.javaecommerce.model.entity.UserEntity;
import com.example.javaecommerce.model.request.UserRequest;
import lombok.RequiredArgsConstructor;


import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
public class RegisterTestApi {
    public static UserRequest prepareUserForRequesting() {
        return UserRequest.builder()
                .email("thoa07@gmail.com")
                .password("123123")
                .build();
    }

    public static UserRequest prepareUserBlankDataForRequesting() {
        return UserRequest.builder()
                .email("")
                .password("")
                .build();
    }

    public static RoleEntity prepareRoleUser() {
        return RoleEntity.builder()
                .name(ERole.ROLE_USER)
                .build();
    }

    public static UserEntity makeUserForSaving(final Long userId) {
        Set<RoleEntity> roles = new HashSet<>();
        roles.add(prepareRoleUser());
        return UserEntity.builder()
                .id(userId)
                .email("thoa07@gmail.com")
                .password("123123")
                .roles(roles)
                .build();
    }

}
