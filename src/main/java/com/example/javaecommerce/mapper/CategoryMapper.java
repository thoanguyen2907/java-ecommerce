package com.example.javaecommerce.mapper;

import com.example.javaecommerce.model.entity.CategoryEntity;
import com.example.javaecommerce.model.request.CategoryRequest;
import com.example.javaecommerce.model.response.CategoryResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryResponse toCategoryResponse(CategoryEntity categoryEntity);
    List<CategoryResponse>  toListCategoryResponse(List<CategoryEntity> categoryEntityList);
    CategoryEntity toCategoryEntity(CategoryRequest categoryRequest);
}
