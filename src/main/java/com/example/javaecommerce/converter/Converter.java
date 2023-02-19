package com.example.javaecommerce.converter;
import com.example.javaecommerce.model.entity.UserEntity;
import com.example.javaecommerce.model.response.UserResponse;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.NameTokenizers;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class Converter {
    public static <T> T toModel(Object obj, Class<T> zClass) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSourceNameTokenizer(NameTokenizers.UNDERSCORE);
        return (T) modelMapper.map(obj, zClass);
    }

    public static <T, Y> List<T> toList(List<Y> list, Class<T> zClass) {
        return list.stream().map(e -> toModel(e, zClass)).collect(Collectors.toList());
    }

    public static  <T, Y> List<T>  toList(Page<UserEntity> pageUserList,Class<T> zClass) {
        return pageUserList.stream().map(e -> toModel(e, zClass)).collect(Collectors.toList());
    }
}