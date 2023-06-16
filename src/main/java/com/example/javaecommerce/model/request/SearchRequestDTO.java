package com.example.javaecommerce.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchRequestDTO {
    private String column;
    private String value;
    private Operation operation;
    private String joinTable;

    public enum Operation {
        EQUAL, LIKE, IN, GREATER_THAN, LESS_THAN, BETWEEN, JOIN
    }
}
