package com.example.javaecommerce.service;

import com.example.javaecommerce.model.request.ProductRequest;
import com.example.javaecommerce.model.response.ProductResponse;

import java.util.List;

public interface ProductService {
    List<ProductResponse> getAllProducts();


    ProductResponse getProductById(Long productID);

    void deleteProduct(Long productID);

    ProductResponse addProduct(ProductRequest productRequest);

    ProductResponse updateProduct(ProductRequest productRequest, Long id);

    int calculateRating(Long productID, int rating);
}
