package com.example.javaecommerce.category;

import com.example.javaecommerce.model.entity.CategoryEntity;
import com.example.javaecommerce.model.request.CategoryRequest;
import com.example.javaecommerce.model.response.CategoryResponse;


public final class CategoryTestApi {
    public static CategoryEntity makeCategoryForSaving(final Long categoryId) {
        return CategoryEntity.builder()
                .id(categoryId)
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
                .id(categoryEntity.getId())
                .name(categoryEntity.getName())
                .build();
    }

    public static CategoryRequest prepareCategoryForRequestingUpdate(final String name) {
        return CategoryRequest.builder()
                .name(name)
                .build();
    }
}
