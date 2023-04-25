package com.example.javaecommerce.model.response;

import com.example.javaecommerce.model.entity.OrderDetailEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class OrderResponse {
    private String address;
    private String city;
    private String country;
    private String email;
    private String postal_code;
    private String first_name;
    private String last_name;
    private String phone;
    //  private float subtotal;
    //private boolean status;
    private List<OrderDetailEntity> orderDetails = new ArrayList<OrderDetailEntity>();

}
