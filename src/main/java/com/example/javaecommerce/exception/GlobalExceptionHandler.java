package com.example.javaecommerce.exception;

import com.example.javaecommerce.model.response.ResponseEntityBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EcommerceRunTimeException.class)
    public ResponseEntity<?> handleEcommerceRunTimeException(final EcommerceRunTimeException ex) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(ex.getStatus())
                .error(ex.getCode())
                .message(ex.getMessage())
                .build();

        return ResponseEntityBuilder.getBuilder()
                .setCode(ex.getStatus())
                .setMessage(ex.getMessage())
                .setDetails(errorResponse)
                .build();
    }

    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<?> handleHttpRequestMethodNotSupportedException(final HttpRequestMethodNotSupportedException e) {
        return ResponseEntityBuilder
                .getBuilder()
                .setCode(e.hashCode())
                .setMessage(e.getMessage())
                .build();
    }
}