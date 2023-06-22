package com.example.javaecommerce.category;

import com.example.javaecommerce.model.entity.CategoryEntity;
import com.example.javaecommerce.model.request.CategoryRequest;
import com.example.javaecommerce.model.response.CategoryResponse;


public final class CategoryTestApi {
    public static CategoryEntity makeCategoryForSaving() {
        return CategoryEntity.builder()
                .name("Category A")
                .build();
    }

    public static CategoryRequest prepareCategoryForRequesting() {
        return CategoryRequest.builder()
                .name("Category A")
                .build();
    }

    public static CategoryResponse toCategoryResponse(final CategoryEntity categoryEntity) {
        return CategoryResponse.builder()
                .name(categoryEntity.getName())
                .build();
    }
}
