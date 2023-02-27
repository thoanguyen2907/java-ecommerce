package com.example.javaecommerce.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDetailRequest {
    private float price;
    private int quantity;
    private float total;
    private Long productId;
}
