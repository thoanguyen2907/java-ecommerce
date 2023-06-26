package com.example.javaecommerce.category;

import com.example.javaecommerce.IntegrationTestUtil;
import com.example.javaecommerce.exception.ResourceNotFoundException;
import com.example.javaecommerce.model.entity.CategoryEntity;
import com.example.javaecommerce.model.entity.ProductEntity;
import com.example.javaecommerce.model.request.CategoryRequest;

import com.example.javaecommerce.model.response.CategoryResponse;
import com.example.javaecommerce.repository.CategoryRepository;
import com.example.javaecommerce.repository.ProductRepository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import java.util.Collections;
import java.util.Optional;
import java.util.Random;

import static com.example.javaecommerce.ResponseBodyMatcher.responseBody;
import static com.example.javaecommerce.category.CategoryTestApi.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CategoryApiDelegateImplTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryRepository categoryRepository;
    @MockBean
    private ProductRepository productRepository;
    @Autowired
    private ObjectMapper objectMapper;
    private Long categoryId;
    private CategoryRequest categoryRequest;
    private CategoryEntity category;

    @Before
    public void setUp() {
        categoryId = new Random().nextLong();
        category = makeCategoryForSaving(categoryId);
        categoryRequest = prepareCategoryForRequesting();
    }

    @Test
    public void givenMoreComplexCategoryData_whenSendData_thenReturnsCategoryCreated() throws Exception {
        Mockito.when(categoryRepository.save(Mockito.any())).thenReturn(category);
        var expectedCategory = toCategoryResponse(category);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/category")
                        .content(IntegrationTestUtil.asJsonString(categoryRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(expectedCategory.getName()));
    }

    @Test
    public void givenMissingNameAttribute_whenAddCategory_thenReturnBadRequest() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/category")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"description\":\"Category without name attribute\"}");
        //act
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        //assert
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus(),
                "Incorrect HTTP status code returned");
    }

    @Test
    public void givenValidCategoryId_whenDeleteCategory_thenReturnNoContent() throws Exception {
        Mockito.when(productRepository.findAll()).thenReturn(Collections.emptyList());
        Mockito.doNothing().when(categoryRepository).deleteById(categoryId);
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/category/{categoryId}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    public void givenInvalidCategoryId_whenDeleteCategory_thenReturnsProductExistedError() throws Exception {
        categoryId = 100L;

        CategoryEntity category = CategoryEntity.builder()
                .id(categoryId)
                .name("Category A")
                .build();

        ProductEntity product = ProductEntity.builder()
                .id(1L)
                .name("Product A")
                .category(category)
                .build();

        Mockito.when(productRepository.findAll()).thenReturn(Collections.singletonList(product));
        Mockito.doNothing().when(categoryRepository).deleteById(categoryId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/category/{categoryId}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("product is existed"));
    }

    @Test
    public void givenValidCategoryId_whenGetCategoryById_thenReturnCategory() throws Exception {
        Mockito.when(categoryRepository.findById(categoryId)).thenReturn(Optional.ofNullable(category));
        var expectedCategory = toCategoryResponse(category);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/category/{categoryId}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(responseBody().containsObjectBody(expectedCategory, CategoryResponse.class, objectMapper));
    }

    @Test
    public void givenValidCategoryId_whenGetCategoryById_thenThrowException() throws Exception {
        Long categoryIdRandom = 100L;

        Mockito.when(categoryRepository.findById(categoryIdRandom)).thenThrow(new ResourceNotFoundException("Category", "id", categoryIdRandom));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/category/{categoryId}", categoryIdRandom)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
