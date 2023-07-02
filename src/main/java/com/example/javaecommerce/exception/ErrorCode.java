package com.example.javaecommerce.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode implements CommonErrorCode {
    SUCCESS(HttpStatus.OK, "000", "Success"),
    FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "999", "System error"),
    ID_NOT_FOUND(HttpStatus.NOT_FOUND, "404", "Could not find the Id"),
    SHOULD_NOT_BLANK(HttpStatus.BAD_REQUEST, "400", "Attribute should not be blank"),
    ITEM_EXISTED(HttpStatus.BAD_REQUEST, "400", "Item is existed"),
    API_NOT_FOUND(HttpStatus.NOT_FOUND, "404", "API Not Found"),
    AUTHORIZATION_FIELD_MISSING(HttpStatus.FORBIDDEN, "403", "Please log in"),
    SIGNATURE_NOT_CORRECT(HttpStatus.FORBIDDEN, "40001", "Signature not correct"),
    EXPIRED(HttpStatus.FORBIDDEN, "40003", "Expired"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "403", "Unauthorized"),
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "400", "Invalid token"),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "401", "Token is expired"),
    UNAUTHENTICATED(HttpStatus.UNAUTHORIZED, "401", "Unauthenticated. Please log in"),
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "400", "validation.error"),
    ALREADY_EXIST(HttpStatus.INTERNAL_SERVER_ERROR, "500", "Account already exist!"),
    USER_NAME_PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST, "400", "Username or password not match!"),
    JWT_CLAIM_EMPTY(HttpStatus.UNAUTHORIZED, "401", "Claim empty");

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
