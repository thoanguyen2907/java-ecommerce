package com.example.javaecommerce.controller;

import com.example.javaecommerce.model.request.CategoryRequest;
import com.example.javaecommerce.model.response.CategoryResponse;
import com.example.javaecommerce.pagination.PaginationPage;
import com.example.javaecommerce.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "api/v1/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;


    @GetMapping
    public PaginationPage<CategoryResponse> getAllCategoriesPagination(@RequestParam(name = "offset", defaultValue = "1") final Integer offset,
                                                                       @RequestParam(name = "limit", defaultValue = "10") final Integer limit) {
        var categoryList = categoryService.getAllCategoriesPagination(offset, limit);
        return categoryList;
    }

    @GetMapping(path = "{categoryId}")
    public CategoryResponse getCategoryById(@PathVariable("categoryId") Long categoryId) {
        return categoryService.getCategoryById(categoryId);
    }

    @PostMapping
    public ResponseEntity<?> addCategory(@Valid @RequestBody CategoryRequest categoryRequest) {
        CategoryResponse categoryResponse = categoryService.addCategory(categoryRequest);
        return ResponseEntity.ok(categoryResponse);
    }

    @DeleteMapping(path = "{categoryId}")
    public void deleteCategory(@PathVariable("categoryId") Long categoryId) throws Exception {
        categoryService.deleteCategory(categoryId);
    }

    @PutMapping
    public ResponseEntity<?> updateCategory(@RequestBody CategoryRequest categoryRequest, @PathVariable Long id) {
        CategoryResponse categoryResponse = categoryService.updateCategory(categoryRequest, id);
        return ResponseEntity.ok(categoryResponse);
    }
}