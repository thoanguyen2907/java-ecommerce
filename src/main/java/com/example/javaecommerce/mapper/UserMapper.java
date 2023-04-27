package com.example.javaecommerce.mapper;

import com.example.javaecommerce.model.entity.UserEntity;
import com.example.javaecommerce.model.response.UserResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toUserResponse(UserEntity userEntity);
    List<UserResponse> toListUserResponse(List<UserEntity> userEntityList);
}
