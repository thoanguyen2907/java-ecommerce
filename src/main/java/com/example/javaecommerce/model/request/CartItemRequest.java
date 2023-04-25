package com.example.javaecommerce.model.request;

import com.example.javaecommerce.model.entity.ProductEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemRequest {
    private String productId;
    private float price;
    private int quantity;
}
