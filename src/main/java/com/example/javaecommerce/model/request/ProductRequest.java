package com.example.javaecommerce.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequest {
    private String name;
    private String brand;
    private String description;
    private int rating;
    private int price;
    private int countInStock;
    private Long categoryId;
}
