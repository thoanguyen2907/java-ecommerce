package com.example.javaecommerce.controller;

import com.example.javaecommerce.model.request.ProductRequest;
import com.example.javaecommerce.model.response.ProductResponse;
import com.example.javaecommerce.pagination.PaginationPage;
import com.example.javaecommerce.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "api/v1/products")
@RequiredArgsConstructor
@Valid
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<?> getAllProducts(@RequestParam(name = "offset", defaultValue = "1") final Integer offset,
                                            @RequestParam(name = "limit", defaultValue = "3") final Integer limit) {
        PaginationPage<ProductResponse> productResponses = productService.getAllProducts(offset, limit);
        return ResponseEntity.ok(productResponses);
    }

    @GetMapping(path = "{productId}")
    public ResponseEntity<?> getProductById(@PathVariable("productId") Long productId) {
        ProductResponse productResponse = productService.getProductById(productId);
        return ResponseEntity.ok(productResponse);
    }

    @PostMapping
    public ResponseEntity<?> addProduct(@RequestBody ProductRequest productRequest) {
        ProductResponse productResponse = productService.addProduct(productRequest);
        return ResponseEntity.ok(productResponse);
    }

    @PostMapping(value = "/rating/{productId}/{rating}")
    public int calculateAverageRating(@PathVariable("productId") Long productId, @PathVariable("rating") int rating) {
        int averageRating = productService.calculateRating(productId, rating);
        return averageRating;
    }

    @DeleteMapping(path = "{productId}")
    public void deleteProduct(@PathVariable("productId") Long productId) {
        productService.deleteProduct(productId);
    }

    @PutMapping(path = "{productId}")
    public ResponseEntity<?> updateProduct(@RequestBody ProductRequest productRequest, @PathVariable Long id) {
        ProductResponse productResponse = productService.updateProduct(productRequest, id);
        return ResponseEntity.ok(productResponse);
    }

    @GetMapping("/category/{categoryId}/")
    public ResponseEntity<?> getAllProductByCategoryId(@PathVariable(value = "categoryId") Long categoryId) {
        List<ProductResponse> productListByCategoryId = productService.getProductListByCategoryId(categoryId);
        return ResponseEntity.ok(productListByCategoryId);
    }

    @DeleteMapping("/category/{categoryId}")
    public ResponseEntity<?> deleteAllProductOfCategory(@PathVariable(value = "categoryId") Long categoryId) {
        productService.deleteProductsOfCategory(categoryId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
