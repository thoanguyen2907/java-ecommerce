package com.example.javaecommerce.category;

import com.example.javaecommerce.IntegrationTestUtil;
import com.example.javaecommerce.exception.ResourceNotFoundException;
import com.example.javaecommerce.mapper.CategoryMapper;
import com.example.javaecommerce.model.entity.CategoryEntity;

import com.example.javaecommerce.model.entity.ProductEntity;

import com.example.javaecommerce.model.request.CategoryRequest;

import com.example.javaecommerce.model.response.CategoryResponse;

import com.example.javaecommerce.pagination.OffsetPageRequest;
import com.example.javaecommerce.repository.CategoryRepository;

import com.example.javaecommerce.repository.ProductRepository;
;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.*;

import static com.example.javaecommerce.ResponseBodyMatcher.responseBody;
import static com.example.javaecommerce.category.CategoryTestApi.*;

import static org.hamcrest.Matchers.hasSize;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;

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
    @Autowired
    private CategoryMapper categoryMapper;
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
        when(categoryRepository.save(Mockito.any())).thenReturn(category);
        var expectedCategory = toCategoryResponse(category);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/category")
                        .content(IntegrationTestUtil.asJsonString(categoryRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(expectedCategory.getName()));
    }

    @Test
    public void givenMissingNameAttribute_whenAddCategory_thenReturnBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"description\":\"Category without name attribute\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenValidCategoryId_whenDeleteCategory_thenReturnNoContent() throws Exception {
        when(productRepository.findAll()).thenReturn(Collections.emptyList());
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

        when(productRepository.findAll()).thenReturn(Collections.singletonList(product));
        Mockito.doNothing().when(categoryRepository).deleteById(categoryId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/category/{categoryId}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("product is existed"));
    }

    @Test
    public void givenValidCategoryId_whenGetCategoryById_thenReturnCategory() throws Exception {
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.ofNullable(category));
        var expectedCategory = toCategoryResponse(category);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/category/{categoryId}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(responseBody().containsObjectBody(expectedCategory, CategoryResponse.class, objectMapper));
    }

    @Test
    public void givenInvalidCategoryId_whenGetCategoryById_thenThrowException() throws Exception {
        Long categoryIdRandom = 100L;

        when(categoryRepository.findById(categoryIdRandom))
                .thenThrow(new ResourceNotFoundException("Category", "id", categoryIdRandom));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/category/{categoryId}", categoryIdRandom)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenExistingCategory_whenUpdatingCategory_thenReturnsUpdatedCategory() throws Exception {
        //find category - mock when
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.ofNullable(category));
        //prepare request category to update
        var categoryUpdateRequest = prepareCategoryForRequestingUpdate("Category B");
        //save updated category
        var categoryEntityUpdate = categoryMapper.toCategoryEntity(categoryUpdateRequest);
        when(categoryRepository.save(Mockito.any(CategoryEntity.class))).thenReturn(categoryEntityUpdate);
        //prepare expected response
        var expectedCategory = categoryMapper.toCategoryResponse(categoryEntityUpdate);
        //perform the API request and validate the response
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/category/{categoryId}", categoryId)
                        .content(IntegrationTestUtil.asJsonString(categoryUpdateRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(responseBody().containsObjectBody(expectedCategory, CategoryResponse.class, objectMapper));
    }

    @Test
    public void givenNotExistingCategoryId_whenUpdatingCategory_thenReturnsBadRequestExceptions() throws Exception {
        Long categoryIdRandom = 100L;
        // find category throw error
        when(categoryRepository.findById(categoryIdRandom))
                .thenThrow(new ResourceNotFoundException("Category", "id", categoryIdRandom));
        // perform the API request and validate the response
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/category/{categoryId}", categoryIdRandom))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenInvalidRequest_whenUpdateCategory_thenReturnsNotFound() throws Exception {
        Long categoryId = 1L;
        // Send an invalid request with an empty category name
        CategoryRequest invalidRequest = new CategoryRequest();
        invalidRequest.setName("");
        // perform the API request and validate the response
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/category/{categoryId}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenOffsetAndLimit_whenGetAllCategoriesPagination_thenReturnsCategories() throws Exception {
        int offset = 0;
        int limit = 10;

        // Create a list of category entities
        List<CategoryEntity> categoryEntities = Arrays.asList(
                new CategoryEntity(1L, "Category 1"),
                new CategoryEntity(2L, "Category 2"),
                new CategoryEntity(3L, "Category 3")
        );

        // Mock the repository response
        Pageable pageable = new OffsetPageRequest(offset, limit);
        Page<CategoryEntity> page = new PageImpl<>(categoryEntities, pageable, categoryEntities.size());
        when(categoryRepository.findAll(pageable)).thenReturn(page);

        // Perform the API request and validate the response
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/category")
                        .param("offset", String.valueOf(offset))
                        .param("limit", String.valueOf(limit))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalRecords", is(categoryEntities.size())))
                .andExpect(jsonPath("$.offset", is(offset)))
                .andExpect(jsonPath("$.limit", is(limit)))
                .andExpect(jsonPath("$.records", hasSize(categoryEntities.size())))
                .andExpect(jsonPath("$.records[0].id", is(categoryEntities.get(0).getId().intValue())))
                .andExpect(jsonPath("$.records[0].name", is(categoryEntities.get(0).getName())))
                .andExpect(jsonPath("$.records[1].id", is(categoryEntities.get(1).getId().intValue())))
                .andExpect(jsonPath("$.records[1].name", is(categoryEntities.get(1).getName())));

    }

}

