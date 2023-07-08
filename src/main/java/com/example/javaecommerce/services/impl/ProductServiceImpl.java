package com.example.javaecommerce.services.impl;


import com.example.javaecommerce.exception.EcommerceRunTimeException;
import com.example.javaecommerce.exception.ErrorCode;
import com.example.javaecommerce.mapper.ProductMapper;
import com.example.javaecommerce.model.entity.CategoryEntity;
import com.example.javaecommerce.model.entity.ProductEntity;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Override
    public PaginationPage<ProductResponse> getAllProducts(final Integer offset, final Integer limit) {
        try {
            var pageable = new OffsetPageRequest(offset, limit);
            var productList = productRepository.findAll(pageable);
            var productResponse = productList
                    .getContent()
                    .stream()
                    .map(item -> productMapper.toProductResponse(item))
                    .collect(Collectors.toList());
            logger.info("Get all products with pagination successfully");
            return new PaginationPage<ProductResponse>()
                    .setLimit(productList.getSize())
                    .setTotalRecords(productList.getTotalElements())
                    .setOffset(productList.getSize())
                    .setRecords(productResponse);
        } catch (Exception e) {
            logger.info("Failed to get all products with pagination", e);
            throw new RuntimeException("Failed to get all products with pagination");
        }
    }

    @Override
    public PaginationPage<ProductResponse> getAllProductsWithSearch(final Integer offset, final Integer limit,
                                                                    final RequestDTO requestDTO) {
        try {
            var pageable = new OffsetPageRequest(offset, limit);
            Specification<ProductEntity> productSpecification = filterSpecification
                    .getSearchSpecification(requestDTO.getSearchRequestDTOList(), requestDTO.getGlobalOperator());
            logger.info("product specification ", productSpecification);
            var productList = productRepository.findAll(productSpecification, pageable);
            logger.info("product list with specification and pagination");
            var productResponse = productList.getContent()
                    .stream()
                    .map(item -> productMapper.toProductResponse(item))
                    .collect(Collectors.toList());
            logger.info("Get all products  with pagination and search specification successfully");
            return new PaginationPage<ProductResponse>()
                    .setLimit(productList.getSize())
                    .setTotalRecords(productList.getTotalElements())
                    .setOffset(productList.getSize())
                    .setRecords(productResponse);
        } catch (Exception e) {
            logger.info("Failed to get all products with pagination", e);
            throw new RuntimeException("Failed to get all products with pagination");
        }
    }

    @Override
    public ProductResponse addProduct(final ProductRequest productRequest) {
        try {
            ProductEntity productEntity = productMapper.toProductEntity(productRequest);
            CategoryEntity category = categoryRepository.findById(productRequest.getCategoryId())
                    .orElseThrow(() -> new EcommerceRunTimeException(ErrorCode.ID_NOT_FOUND)
                    );
            productEntity.setCategory(category);
            var result = productRepository.save(productEntity);
            logger.info("create product successfully");
            return productMapper.toProductResponse(result);
        } catch (Exception e) {
            logger.info("Failed to create product");
            throw new RuntimeException("Failed to create product", e);
        }
    }

    @Override
    public ProductResponse updateProduct(final ProductRequest productRequest, final Long id) {
        try {
            ProductEntity productEntity = productRepository.findById(id)
                    .map(product -> {
                        product.setName(productRequest.getName());
                        product.setBrand(productRequest.getBrand());
                        product.setDescription(productRequest.getDescription());
                        product.setCountInStock(productRequest.getCountInStock());
                        product.setRating(productRequest.getRating());
                        return productRepository.save(product);
                    }).orElseThrow(() -> new EcommerceRunTimeException(ErrorCode.ID_NOT_FOUND));
            logger.info("Update product successfully by id {} ", id);
            return productMapper.toProductResponse(productEntity);
        } catch (Exception e) {
            logger.info("Failed to update product", e);
            throw new RuntimeException("Failed to update product");
        }
    }

    @Override
    public void deleteProduct(final Long productId) {
        try {
            ProductEntity product = productRepository
                    .findById(productId)
                    .orElseThrow(() -> new EcommerceRunTimeException(ErrorCode.ID_NOT_FOUND));
            logger.info("Product is found with id {}", productId);
            productRepository.deleteById(product.getId());
            logger.info("Delete product by id successfully {} ", productId);
        } catch (Exception e) {
            logger.info("Failed to delete product", e);
            throw new RuntimeException("Failed to delete product");
        }
    }

    @Override
    public ProductResponse getProductById(final Long productId) {
        try {
            ProductEntity product = productRepository.findById(productId)
                    .orElseThrow(() -> new EcommerceRunTimeException(ErrorCode.ID_NOT_FOUND));
            logger.info("product is found by id {}", product.getId());
            return productMapper.toProductResponse(product);
        } catch (Exception e) {
            logger.info("Failed to get product by id", e);
            throw new RuntimeException("Failed to get product by id");
        }
    }

    @Transient
    public int calculateRating(final Long productId, final int rating) {
        ProductEntity productEntity = productRepository
                .findById(productId).orElseThrow(() -> new EcommerceRunTimeException(ErrorCode.ID_NOT_FOUND));
        logger.info("Product is found by id {}", productId);
        int currentRating = productEntity.getRating();
        int averageRating = (currentRating + rating) / 2;
        productEntity.setRating(averageRating);
        return averageRating;
    }

    @Override
    public List<ProductResponse> getProductListByCategoryId(final Long categoryId) {
        try {
            if (!categoryRepository.existsById(categoryId)) {
                logger.info("Category is not existed {} ", categoryId);
                throw new EcommerceRunTimeException(ErrorCode.ID_NOT_FOUND);
            }
            logger.info("Category with id is existed {} ", categoryId);
            List<ProductEntity> productEntities = productRepository.findByCategoryId(categoryId);
            logger.info("Get list of products successfully");
            return productMapper.toListProductResponse(productEntities);
        } catch (Exception e) {
            logger.info("Failed to get list of products", e);
            throw new RuntimeException("Failed to get list of products");
        }
    }

    @Override
    public void deleteProductsOfCategory(final Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            logger.info("Category is not existed {} ", categoryId);
            throw new EcommerceRunTimeException(ErrorCode.ID_NOT_FOUND);
        }
        logger.info("delete product by category successfully {}", categoryId);
        productRepository.deleteByCategoryId(categoryId);
    }

}
