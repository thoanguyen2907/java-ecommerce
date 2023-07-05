package com.example.javaecommerce.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class EcommerceRunTimeException extends RuntimeException {

    private String code;
    private String message;
    private HttpStatus status;

    public EcommerceRunTimeException() {
    }

    public EcommerceRunTimeException(final CommonErrorCode code) {
        this.code = code.code();
        this.message = code.message();
        this.status = code.status();
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public HttpStatus getStatus() {
        return this.status;
    }

    public void setStatus(final HttpStatus status) {
        this.status = status;
    }
}
