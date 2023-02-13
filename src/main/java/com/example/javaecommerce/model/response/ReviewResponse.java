package com.example.javaecommerce.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewResponse {
    private Long id;
    private  Long productId;
    private  float rating;
}
