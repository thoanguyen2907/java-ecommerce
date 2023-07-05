package com.example.javaecommerce.model.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RequestDTO {
    private List<SearchRequestDTO> searchRequestDTOList;
    private GlobalOperator globalOperator;

    public enum GlobalOperator {
        AND, OR
    }
}
