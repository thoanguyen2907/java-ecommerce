package com.example.javaecommerce.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class SignupRequest {
    @NotBlank(message = "email must not be empty")
    private String email;
    @NotBlank(message = "password must not be empty")
    private String password;
}
