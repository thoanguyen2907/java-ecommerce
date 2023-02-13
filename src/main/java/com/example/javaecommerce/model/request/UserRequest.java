package com.example.javaecommerce.model.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UserRequest {
    private String email;
    private String password;

    private Set<String> role;
}
