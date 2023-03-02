package com.example.javaecommerce.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderDetailRequest {
    private float price;
    private int quantity;
    private float total;
    private Long productId;
}
