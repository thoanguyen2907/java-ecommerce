package com.example.javaecommerce.model.entity;

import com.example.javaecommerce.model.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
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
    private String postalCode;
    private String firstName;
    private String lastName;
    private String phone;
    private float totalPrice;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY,  cascade = CascadeType.ALL)
    private List<OrderDetailEntity> orderDetails = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
};
