package com.example.javaecommerce.service.impl;

import com.example.javaecommerce.converter.Converter;
import com.example.javaecommerce.model.entity.CategoryEntity;
import com.example.javaecommerce.model.entity.ProductEntity;
import com.example.javaecommerce.model.entity.ReviewEntity;
import com.example.javaecommerce.model.request.ProductRequest;
import com.example.javaecommerce.model.response.ProductResponse;
import com.example.javaecommerce.repository.CategoryRepository;
import com.example.javaecommerce.repository.ProductRepository;
import com.example.javaecommerce.service.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Transient;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;


    @Override
    public List<ProductResponse> getAllProducts() {
        List<ProductEntity> productEntities = productRepository.findAll();
        return Converter.toList(productEntities, ProductResponse.class);
    }

    @Override
    public ProductResponse addProduct(ProductRequest productRequest) {
        ProductEntity productEntity = Converter.toModel(productRequest, ProductEntity.class);
        CategoryEntity category = categoryRepository.findById(productRequest.getCategoryId())
                .orElseThrow(() -> new IllegalStateException(
                        "category with id " + productRequest.getCategoryId() + " does not exist"));
        productEntity.setCategory(category);
        productRepository.save(productEntity);
        return Converter.toModel(productEntity, ProductResponse.class);
    }

    @Override
    public ProductResponse updateProduct(ProductRequest productRequest, Long id) {
        ProductEntity productEntity = productRepository.findById(id)
                .map(product -> {
                    product.setName(productRequest.getName());
                    product.setBrand(productRequest.getBrand());
                    product.setDescription(productRequest.getDescription());
                    product.setCount_in_stock(productRequest.getCount_in_stock());
                    product.setRating(productRequest.getRating());
                    return productRepository.save(product);
                }).orElseThrow(() -> new IllegalStateException(
                        "product with id " + id + " does not exist"));
        return Converter.toModel(productEntity, ProductResponse.class);
    }
    @Override
    public void deleteProduct(Long productID) {
        productRepository.deleteById(productID);
    }

    @Override
    public ProductResponse getProductById(Long productID) {
        ProductEntity productEntity = productRepository.findById(productID).orElseThrow(() -> new IllegalStateException(
                "product with id " + " does not exist"
        ));

        return Converter.toModel(productEntity, ProductResponse.class);

    }
    @Transient
    public int calculateRating(Long productID, int rating) {
        ProductEntity productEntity = productRepository.findById(productID).orElseThrow(() -> new IllegalStateException(
                "product with id " + " does not exist"
        ));
       ReviewEntity reviewEntity = new ReviewEntity();
        int currentRating = productEntity.getRating();
        int averageRating =  (currentRating + rating) / 2;
        productEntity.setRating(averageRating);
        reviewEntity.setProduct(productEntity);
        return averageRating;
    }

}
