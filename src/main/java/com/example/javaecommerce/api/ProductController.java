package com.example.javaecommerce.api;

import com.example.javaecommerce.model.request.ProductRequest;
import com.example.javaecommerce.model.response.ProductResponse;
import com.example.javaecommerce.service.ProductService;
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
    public ResponseEntity<?> getAllProducts() {
        List<ProductResponse> productResponses = productService.getAllProducts();
        return ResponseEntity.ok(productResponses);
    }

    @GetMapping(path = "{productId}")
    public ResponseEntity<?> getProductById(@PathVariable("productId") Long productID) {
        ProductResponse productResponse = productService.getProductById(productID);
        return ResponseEntity.ok(productResponse);
    }

    @PostMapping
    public ResponseEntity<?> addProduct(@RequestBody ProductRequest productRequest) {
        ProductResponse productResponse = productService.addProduct(productRequest);
        return ResponseEntity.ok(productResponse);
    }

    @PostMapping(value = "/rating/{productId}/{rating}")
    public int calculateAverageRating(@PathVariable("productId") Long productID, @PathVariable("rating") int rating) {
        int average_rating = productService.calculateRating(productID, rating);
        return average_rating;
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
