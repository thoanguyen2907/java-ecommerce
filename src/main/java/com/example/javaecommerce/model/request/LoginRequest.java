package com.example.javaecommerce.model.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class LoginRequest {
    private String email;
    private String password;
}
