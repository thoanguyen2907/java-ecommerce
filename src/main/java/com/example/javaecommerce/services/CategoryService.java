package com.example.javaecommerce.services;

import com.example.javaecommerce.model.request.CategoryRequest;
import com.example.javaecommerce.model.response.CategoryResponse;
import com.example.javaecommerce.pagination.PaginationPage;

import java.util.List;

public interface CategoryService {
    List<CategoryResponse> getAllCategories();

    PaginationPage<CategoryResponse> getAllCategoriesPagination(Integer offset, Integer limited);

    CategoryResponse addCategory(CategoryRequest category);

    CategoryResponse getCategoryById(Long categoryId);

    void deleteCategory(Long categoryId) throws Exception;

    CategoryResponse updateCategory(CategoryRequest categoryRequest, Long id);

}
