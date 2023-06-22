package com.example.javaecommerce;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.assertj.core.api.Assertions.assertThat;

public class ResponseBodyMatcher {
    public static ResponseBodyMatcher responseBody() {
        return new ResponseBodyMatcher();
    }

    public <T> ResultMatcher containsObjectBody(final Object objectExpected, final Class<T> targetClass, final ObjectMapper objectMapper) {
        return mvc -> {
            String json = mvc.getResponse().getContentAsString();
            T actualObject = objectMapper.readValue(json, targetClass);
            assertThat(actualObject).usingRecursiveComparison().isEqualTo(objectExpected);
        };
    }
}
