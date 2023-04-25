package com.example.javaecommerce.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDetailResponse {
    private String email;
    private float price;
    private int quantity;
    private float total;
}
