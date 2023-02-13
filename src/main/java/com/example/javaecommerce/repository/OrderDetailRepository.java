package com.example.javaecommerce.repository;
import com.example.javaecommerce.model.entity.OrderDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailRepository  extends JpaRepository<OrderDetailEntity, Long> {
}
