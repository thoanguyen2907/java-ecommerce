package com.example.javaecommerce.exception;


import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ErrorResponse {
    private HttpStatus status;
    private String error;
    private String message;
}

