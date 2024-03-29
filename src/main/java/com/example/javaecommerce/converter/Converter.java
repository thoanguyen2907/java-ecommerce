package com.example.javaecommerce.converter;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.NameTokenizers;

import java.util.List;
import java.util.stream.Collectors;

public class Converter {
    public static <T> T toModel(final Object obj, final Class<T> zClass) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSourceNameTokenizer(NameTokenizers.UNDERSCORE);
        return (T) modelMapper.map(obj, zClass);
    }

    public static <T, Y> List<T> toList(final List<Y> list, final Class<T> zClass) {
        return list.stream().map(e -> toModel(e, zClass)).collect(Collectors.toList());
    }
}