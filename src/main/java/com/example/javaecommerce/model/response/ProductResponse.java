package com.example.javaecommerce.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductResponse {
    private String name;
    private String brand;
    private String description;
    private int rating;
    private int price;
    private int countInStock;
    private String categoryName;
}
