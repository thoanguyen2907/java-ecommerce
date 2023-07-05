package com.example.javaecommerce.exception;

import org.springframework.http.HttpStatus;

public interface CommonErrorCode {
    String code();

    HttpStatus status();

    String message();
}
