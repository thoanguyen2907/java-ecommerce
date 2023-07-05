package com.example.javaecommerce.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDetailResponse {
    private float price;
    private int quantity;
    private float total;
}
