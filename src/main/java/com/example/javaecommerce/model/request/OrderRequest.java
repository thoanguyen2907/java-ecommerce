package com.example.javaecommerce.model.request;

import com.example.javaecommerce.model.entity.OrderDetailEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class OrderRequest {
    private String address;
    private String city;
    private String country;
    private String email;
    private String postal_code;
    private String first_name;
    private String last_name;
    private String phone;
    private float totalPrice;
    private List<OrderDetailEntity> orderItems;
    private  String userId;
}
