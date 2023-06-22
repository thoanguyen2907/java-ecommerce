package com.example.javaecommerce;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;

@UtilityClass
public class IntegrationTestUtil {
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String asJsonObject(final String obj) {
        try {
            return new ObjectMapper().readValue(obj, String.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
