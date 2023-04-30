package com.example.javaecommerce.model.response;

import com.example.javaecommerce.model.entity.OrderDetailEntity;
import com.example.javaecommerce.model.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class OrderResponse {
    private String address;
    private String city;
    private String country;
    private String email;
    private String postalCode;
    private String firstName;
    private String lastName;
    private String phone;
    private UserResponse user;
    private List<OrderDetailResponse> orderDetails;
}
