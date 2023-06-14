package com.example.javaecommerce.services;

import com.example.javaecommerce.model.entity.ProductEntity;
import com.example.javaecommerce.model.request.ProductRequest;
import com.example.javaecommerce.model.request.RequestDTO;
import com.example.javaecommerce.model.response.ProductResponse;
import com.example.javaecommerce.pagination.PaginationPage;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface ProductService {
    PaginationPage<ProductResponse> getAllProducts(Integer offset, Integer limit);

    PaginationPage<ProductResponse> getAllProductsWithSearch(Integer offset, Integer limit,
                                                             RequestDTO requestDTO);

    ProductResponse getProductById(Long productId);

    void deleteProduct(Long productId);

    ProductResponse addProduct(ProductRequest productRequest);

    ProductResponse updateProduct(ProductRequest productRequest, Long id);

    int calculateRating(Long productId, int rating);

    List<ProductResponse> getProductListByCategoryId(Long categoryId);

    void deleteProductsOfCategory(Long categoryId);
}
