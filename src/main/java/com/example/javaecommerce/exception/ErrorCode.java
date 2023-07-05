package com.example.javaecommerce.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode implements CommonErrorCode {
    ID_NOT_FOUND(HttpStatus.NOT_FOUND, "404", "Could not find the Id"),
    SHOULD_NOT_BLANK(HttpStatus.BAD_REQUEST, "400", "Attribute should not be blank"),
    ITEM_EXISTED(HttpStatus.BAD_REQUEST, "400", "Item is existed"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "403", "Unauthorized"),
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "400", "Invalid token"),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "401", "Token is expired"),
    UNAUTHENTICATED(HttpStatus.UNAUTHORIZED, "401", "Unauthenticated. Please log in"),
    ALREADY_EXIST(HttpStatus.INTERNAL_SERVER_ERROR, "500", "Account already exist!");

    private final HttpStatus status;
    private final String code;
    private final String message;

    private ErrorCode(final HttpStatus status, final String code, final String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public String code() {
        return this.code;
    }

    public HttpStatus status() {
        return this.status;
    }

    public String message() {
        return this.message;
    }
}
