package com.example.javaecommerce.model.entity;

import com.example.javaecommerce.model.base.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "orders_detail")
public class OrderDetailEntity extends BaseEntity {
    private String image;
    private String email;
    private float price;
    private int quantity;
    private float total;
    @ManyToOne()
    @JoinColumn(
            name = "order_id")
    private OrderEntity order;
}
