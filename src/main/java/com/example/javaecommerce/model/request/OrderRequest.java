package com.example.javaecommerce.model.request;


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
    private String postalCode;
    private String firstName;
    private String lastName;
    private String phone;
    private float totalPrice;
    private List<CartItemRequest> cartItems;
    private String userId;
}
