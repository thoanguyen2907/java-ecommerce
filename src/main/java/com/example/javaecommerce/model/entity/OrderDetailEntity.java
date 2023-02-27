package com.example.javaecommerce.model.entity;

import com.example.javaecommerce.model.base.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "orders_detail")
public class OrderDetailEntity extends BaseEntity {
    private float price;
    private int quantity;
    private float total;
    @Column(name = "productId")
    private Long productId;
    @OneToOne
    @JoinColumn(name= "productId")
    private ProductEntity product;
}
