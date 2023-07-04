package com.example.javaecommerce.api;

import com.example.javaecommerce.model.request.CategoryRequest;
import com.example.javaecommerce.model.response.CategoryResponse;
import com.example.javaecommerce.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "api/v1/category")
@RequiredArgsConstructor
public class CategoryController  {
    private  final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<?> getAllCategories() {
        List<CategoryResponse> categoryResponses = categoryService.getAllCategories();
        return ResponseEntity.ok(categoryResponses);
    }
    @GetMapping(path = "{categoryId}")
    public CategoryResponse getCategoryById(@PathVariable("categoryId") Long categoryID) {
        return categoryService.getCategoryById(categoryID);
    }
    @PostMapping
    public ResponseEntity<?> addCategory(@Valid @RequestBody CategoryRequest categoryRequest) {
       CategoryResponse categoryResponse = categoryService.addCategory(categoryRequest);
       return  ResponseEntity.ok(categoryResponse);
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
