package com.example.javaecommerce.services.impl;

import com.example.javaecommerce.converter.Converter;
import com.example.javaecommerce.exception.ResourceNotFoundException;
import com.example.javaecommerce.mapper.ProductMapper;
import com.example.javaecommerce.model.entity.CategoryEntity;
import com.example.javaecommerce.model.entity.ProductEntity;
import com.example.javaecommerce.model.entity.ReviewEntity;
import com.example.javaecommerce.model.request.ProductRequest;
import com.example.javaecommerce.model.response.ProductResponse;
import com.example.javaecommerce.pagination.OffsetPageRequest;
import com.example.javaecommerce.pagination.PaginationPage;
import com.example.javaecommerce.repository.CategoryRepository;
import com.example.javaecommerce.repository.ProductRepository;
import com.example.javaecommerce.services.ProductService;

import javax.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Transient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    @Override
    public PaginationPage<ProductResponse> getAllProducts(Integer offset, Integer limit) {
        var pageable = new OffsetPageRequest(offset, limit);
        var productList = productRepository.findAll(pageable);
        var productResponse = productList
                .getContent()
                .stream()
                .map(item -> productMapper.toProductResponse(item))
                .collect(Collectors.toList());
        return new PaginationPage<ProductResponse>()
                .setLimit(productList.getSize())
                .setTotalRecords(productList.getTotalElements())
                .setOffset(productList.getSize())
                .setRecords(productResponse);
    }

    @Override
    public ProductResponse addProduct(ProductRequest productRequest) {
        ProductEntity productEntity = Converter.toModel(productRequest, ProductEntity.class);
        CategoryEntity category = categoryRepository.findById(productRequest.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("category", "id", productRequest.getCategoryId())
                        );
        productEntity.setCategory(category);
        var result = productRepository.save(productEntity);
        return productMapper.toProductResponse(result);
    }

    @Override
    public ProductResponse updateProduct(ProductRequest productRequest, Long id) {
        ProductEntity productEntity = productRepository.findById(id)
                .map(product -> {
                    product.setName(productRequest.getName());
                    product.setBrand(productRequest.getBrand());
                    product.setDescription(productRequest.getDescription());
                    product.setCountInStock(productRequest.getCountInStock());
                    product.setRating(productRequest.getRating());
                    return productRepository.save(product);
                }).orElseThrow(() -> new ResourceNotFoundException("product", "id", id));
        return productMapper.toProductResponse(productEntity);
    }

    @Override
    public void deleteProduct(Long productId) {
        productRepository.deleteById(productId);
    }

    @Override
    public ProductResponse getProductById(Long productId) {
        ProductEntity productEntity = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("product", "id", productId));

        return productMapper.toProductResponse(productEntity);

    }

    @Transient
    public int calculateRating(Long productId, int rating) {
        ProductEntity productEntity = productRepository.findById(productId).orElseThrow(() -> new IllegalStateException(
                "product with id " + " does not exist"
        ));
        ReviewEntity reviewEntity = new ReviewEntity();
        int currentRating = productEntity.getRating();
        int averageRating = (currentRating + rating) / 2;
        productEntity.setRating(averageRating);
        // reviewEntity.setProduct(productEntity);
        return averageRating;
    }

    @Override
    public List<ProductResponse> getProductListByCategoryId(Long categoryId) {
        if (categoryRepository.existsById(categoryId)) {
            throw new RuntimeException("cant find the categrofy");
        }
        List<ProductEntity> productEntities = productRepository.findByCategoryId(categoryId);

        return productMapper.toListProductResponse(productEntities);
    }

    @Override
    public void deleteProductsOfCategory(Long categoryId) {
        if (categoryRepository.existsById(categoryId)) {
            throw new RuntimeException("cant find category");
        }
        productRepository.deleteByCategoryId(categoryId);
    }

}
