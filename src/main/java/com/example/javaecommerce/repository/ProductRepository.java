package com.example.javaecommerce.repository;

import com.example.javaecommerce.model.entity.ProductEntity;
import com.example.javaecommerce.model.response.ProductResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    List<ProductEntity> findByCategoryId(Long categoryId);

    void deleteByCategoryId(Long categoryId);
}
