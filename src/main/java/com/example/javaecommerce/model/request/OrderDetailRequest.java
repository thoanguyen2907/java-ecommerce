package com.example.javaecommerce.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDetailRequest {
    private String image;
    private String email;
    private float price;
    private int quantity;
    private float total;
}
