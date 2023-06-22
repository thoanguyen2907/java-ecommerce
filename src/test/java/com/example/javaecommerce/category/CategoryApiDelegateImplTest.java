package com.example.javaecommerce.category;

import com.example.javaecommerce.IntegrationTestUtil;
import com.example.javaecommerce.mapper.CategoryMapper;
import com.example.javaecommerce.model.entity.CategoryEntity;
import com.example.javaecommerce.model.request.CategoryRequest;
import com.example.javaecommerce.model.response.CategoryResponse;
import com.example.javaecommerce.repository.CategoryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Random;

import static com.example.javaecommerce.ResponseBodyMatcher.responseBody;
import static com.example.javaecommerce.category.CategoryTestApi.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CategoryApiDelegateImplTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryRepository categoryRepository;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    private Long categoryId;
    private CategoryRequest categoryRequest;
    private CategoryEntity category;

    @Before
    public void setUp() {
        category = makeCategoryForSaving();
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(expectedCategory.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(expectedCategory.getId()));
    }
}
