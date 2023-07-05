package com.example.javaecommerce.model.request;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {
    @NotBlank(message = "email must not be empty")
    private String email;
    @NotBlank(message = "password must not be empty")
    private String password;
}
