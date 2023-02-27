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
@Table(name = "orders")
public class OrderEntity extends BaseEntity {
    private String address;
    private String city;
    private String country;
    private String email;
    private String postal_code;
    private String first_name;
    private String last_name;
    private String phone;
    private float totalPrice;
    @Column(name = "userId")
    private Long userId;
    @ManyToOne()
    @JoinColumn(
            name = "userId")
    private UserEntity user;
    @OneToMany()
    @JoinColumn(name = "orders_id")
    private List<OrderDetailEntity> orderItems;

}
