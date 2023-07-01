package com.example.javaecommerce.controller;

import com.example.javaecommerce.model.request.CategoryRequest;
import com.example.javaecommerce.model.response.CategoryResponse;
import com.example.javaecommerce.pagination.PaginationPage;
import com.example.javaecommerce.services.CategoryService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/api/v1/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;


    @GetMapping
    public PaginationPage<CategoryResponse> getAllCategoriesPagination(@RequestParam(name = "offset", defaultValue = "0") final Integer offset,
                                                                       @RequestParam(name = "limit", defaultValue = "10") final Integer limit) {
        var categoryList = categoryService.getAllCategoriesPagination(offset, limit);
        return categoryList;
    }

    @GetMapping(path = "{categoryId}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable("categoryId") final Long categoryId) {
        return ResponseEntity.ok(categoryService.getCategoryById(categoryId));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addCategory(@Valid @RequestBody final CategoryRequest categoryRequest) {
        CategoryResponse categoryResponse = categoryService.addCategory(categoryRequest);
        return ResponseEntity.ok(categoryResponse);
    }

    @DeleteMapping(path = "{categoryId}")
    public ResponseEntity<?> deleteCategory(@PathVariable("categoryId") final Long categoryId) throws Exception {
        try {
            categoryService.deleteCategory(categoryId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("product is existed");
        }
    }

    @PutMapping(path = "{categoryId}")
    public ResponseEntity<?> updateCategory(@Valid @RequestBody final CategoryRequest categoryRequest,
                                            @PathVariable final Long categoryId) {
        CategoryResponse categoryResponse = categoryService.updateCategory(categoryRequest, categoryId);
        return ResponseEntity.ok(categoryResponse);
    }
}
