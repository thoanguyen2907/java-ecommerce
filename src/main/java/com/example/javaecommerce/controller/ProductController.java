package com.example.javaecommerce.controller;

import com.example.javaecommerce.exception.EcommerceRunTimeException;
import com.example.javaecommerce.exception.ErrorCode;
import com.example.javaecommerce.model.request.ProductRequest;
import com.example.javaecommerce.model.request.RequestDTO;
import com.example.javaecommerce.model.response.ProductResponse;
import com.example.javaecommerce.pagination.PaginationPage;

import com.example.javaecommerce.services.ProductService;

import com.example.javaecommerce.utils.CheckAuthorized;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    @PreAuthorize("hasRole('USER')  or hasRole('ADMIN')")
    public ResponseEntity<?> getAllProducts(@RequestParam(name = "offset", defaultValue = "0") final Integer offset,
                                            @RequestParam(name = "limit", defaultValue = "3") final Integer limit) {
        PaginationPage<ProductResponse> productResponses = productService.getAllProducts(offset, limit);
        return ResponseEntity.ok(productResponses);
    }

    @GetMapping(path = "{productId}")
    public ResponseEntity<?> getProductById(@PathVariable("productId") final Long productId) {
        ProductResponse productResponse = productService.getProductById(productId);
        return ResponseEntity.ok(productResponse);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addProduct(@RequestBody final ProductRequest productRequest) {
        boolean isAdmin = CheckAuthorized.isAuthorized("ADMIN");
        if (!isAdmin) {
            throw new EcommerceRunTimeException(ErrorCode.UNAUTHORIZED);
        }
        ProductResponse productResponse = productService.addProduct(productRequest);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @PostMapping(value = "/rating/{productId}/{rating}")
    public int calculateAverageRating(@PathVariable("productId") final Long productId, @PathVariable("rating") final int rating) {
        int averageRating = productService.calculateRating(productId, rating);
        return averageRating;
    }

    @DeleteMapping(path = "{productId}")
    public void deleteProduct(@PathVariable("productId") final Long productId) {
        productService.deleteProduct(productId);
    }

    @PutMapping(path = "{productId}")
    public ResponseEntity<?> updateProduct(@RequestBody final ProductRequest productRequest, @PathVariable final Long id) {
        ProductResponse productResponse = productService.updateProduct(productRequest, id);
        return ResponseEntity.ok(productResponse);
    }

    @GetMapping("/category/{categoryId}/")
    public ResponseEntity<?> getAllProductByCategoryId(@PathVariable(value = "categoryId") final Long categoryId) {
        List<ProductResponse> productListByCategoryId = productService.getProductListByCategoryId(categoryId);
        return ResponseEntity.ok(productListByCategoryId);
    }

    @DeleteMapping("/category/{categoryId}")
    public ResponseEntity<?> deleteAllProductOfCategory(@PathVariable(value = "categoryId") final Long categoryId) {
        productService.deleteProductsOfCategory(categoryId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/search/specification")
    public PaginationPage<ProductResponse> getAllProductsWithSearch(@RequestBody final RequestDTO requestDTO,
                                                                    @RequestParam(name = "offset", defaultValue = "0") final Integer offset,
                                                                    @RequestParam(name = "limit", defaultValue = "3") final Integer limit) {
        return productService.getAllProductsWithSearch(offset, limit, requestDTO);
    }
}
