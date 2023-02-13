package com.example.javaecommerce.service;
import com.example.javaecommerce.model.request.CategoryRequest;
import com.example.javaecommerce.model.response.CategoryResponse;

import java.util.List;

public interface CategoryService {
    List<CategoryResponse> getAllCategories();

    CategoryResponse addCategory(CategoryRequest category);

    CategoryResponse getCategoryById(Long categoryID);

    void deleteCategory(Long categoryID) throws Exception;

    CategoryResponse updateCategory(CategoryRequest categoryRequest, Long id);

}
