package com.example.javaecommerce.services;

import com.example.javaecommerce.model.request.ProductRequest;
import com.example.javaecommerce.model.response.ProductResponse;
import com.example.javaecommerce.pagination.PaginationPage;

import java.util.List;

public interface ProductService {
    PaginationPage<ProductResponse> getAllProducts(Integer offset, Integer limit);

    ProductResponse getProductById(Long productId);

    void deleteProduct(Long productId);

    ProductResponse addProduct(ProductRequest productRequest);

    ProductResponse updateProduct(ProductRequest productRequest, Long id);

    int calculateRating(Long productId, int rating);

    List<ProductResponse> getProductListByCategoryId(Long categoryId);

    void deleteProductsOfCategory(Long categoryId);
}
