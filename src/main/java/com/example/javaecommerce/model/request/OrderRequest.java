package com.example.javaecommerce.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderRequest {
    private String address;
    private String city;
    private String country;
    private String email;
    private String postal_code;
    private String first_name;
    private String last_name;
    private String phone;
    private float price;
    private int quantity;
    private boolean status;

}
