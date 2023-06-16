package com.example.javaecommerce.mapper;

import com.example.javaecommerce.model.entity.ProductEntity;
import com.example.javaecommerce.model.response.ProductResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(source = "productEntity.category.name", target = "categoryName")
    ProductResponse toProductResponse(ProductEntity productEntity);

    @Mapping(source = "productEntity.category.name", target = "categoryName")
    List<ProductResponse> toListProductResponse(List<ProductEntity> productEntityList);
}
