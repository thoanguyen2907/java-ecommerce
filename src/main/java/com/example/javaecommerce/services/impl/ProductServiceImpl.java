package com.example.javaecommerce.services.impl;

import com.example.javaecommerce.converter.Converter;
import com.example.javaecommerce.exception.EcommerceRunTimeException;
import com.example.javaecommerce.exception.ErrorCode;
import com.example.javaecommerce.mapper.ProductMapper;
import com.example.javaecommerce.model.entity.CategoryEntity;
import com.example.javaecommerce.model.entity.ProductEntity;
import com.example.javaecommerce.model.entity.ReviewEntity;
import com.example.javaecommerce.model.request.ProductRequest;
import com.example.javaecommerce.model.request.RequestDTO;
import com.example.javaecommerce.model.response.ProductResponse;
import com.example.javaecommerce.pagination.OffsetPageRequest;
import com.example.javaecommerce.pagination.PaginationPage;
import com.example.javaecommerce.repository.CategoryRepository;
import com.example.javaecommerce.repository.ProductRepository;
import com.example.javaecommerce.services.ProductService;

import com.example.javaecommerce.utils.FilterSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Transient;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;
    private final FilterSpecification<ProductEntity> filterSpecification;

    @Override
    public PaginationPage<ProductResponse> getAllProducts(final Integer offset, final Integer limit) {
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
    public PaginationPage<ProductResponse> getAllProductsWithSearch(final Integer offset, final Integer limit,
                                                                    final RequestDTO requestDTO) {
        var pageable = new OffsetPageRequest(offset, limit);
        Specification<ProductEntity> productSpecification = filterSpecification
                .getSearchSpecification(requestDTO.getSearchRequestDTOList(), requestDTO.getGlobalOperator());

        var productList = productRepository.findAll(productSpecification, pageable);
        var productResponse = productList.getContent()
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
    public ProductResponse addProduct(final ProductRequest productRequest) {
        ProductEntity productEntity = Converter.toModel(productRequest, ProductEntity.class);
        CategoryEntity category = categoryRepository.findById(productRequest.getCategoryId())
                .orElseThrow(() -> new EcommerceRunTimeException(ErrorCode.ID_NOT_FOUND)
                );
        productEntity.setCategory(category);
        var result = productRepository.save(productEntity);
        return productMapper.toProductResponse(result);
    }

    @Override
    public ProductResponse updateProduct(final ProductRequest productRequest, final Long id) {
        ProductEntity productEntity = productRepository.findById(id)
                .map(product -> {
                    product.setName(productRequest.getName());
                    product.setBrand(productRequest.getBrand());
                    product.setDescription(productRequest.getDescription());
                    product.setCountInStock(productRequest.getCountInStock());
                    product.setRating(productRequest.getRating());
                    return productRepository.save(product);
                }).orElseThrow(() -> new EcommerceRunTimeException(ErrorCode.ID_NOT_FOUND));
        return productMapper.toProductResponse(productEntity);
    }

    @Override
    public void deleteProduct(final Long productId) {
        productRepository.deleteById(productId);
    }

    @Override
    public ProductResponse getProductById(final Long productId) {
        ProductEntity productEntity = productRepository.findById(productId)
                .orElseThrow(() -> new EcommerceRunTimeException(ErrorCode.ID_NOT_FOUND));
        return productMapper.toProductResponse(productEntity);

    }

    @Transient
    public int calculateRating(final Long productId, final int rating) {
        ProductEntity productEntity = productRepository
                .findById(productId).orElseThrow(() -> new EcommerceRunTimeException(ErrorCode.ID_NOT_FOUND));
        ReviewEntity reviewEntity = new ReviewEntity();
        int currentRating = productEntity.getRating();
        int averageRating = (currentRating + rating) / 2;
        productEntity.setRating(averageRating);
        return averageRating;
    }

    @Override
    public List<ProductResponse> getProductListByCategoryId(final Long categoryId) {
        if (categoryRepository.existsById(categoryId)) {
            throw new EcommerceRunTimeException(ErrorCode.ID_NOT_FOUND);
        }
        List<ProductEntity> productEntities = productRepository.findByCategoryId(categoryId);
        return productMapper.toListProductResponse(productEntities);
    }

    @Override
    public void deleteProductsOfCategory(final Long categoryId) {
        if (categoryRepository.existsById(categoryId)) {
            throw new EcommerceRunTimeException(ErrorCode.ID_NOT_FOUND);
        }
        productRepository.deleteByCategoryId(categoryId);
    }

}
