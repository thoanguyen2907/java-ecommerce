package com.example.javaecommerce.service.impl;

import com.example.javaecommerce.converter.Converter;
import com.example.javaecommerce.mapper.CategoryMapper;
import com.example.javaecommerce.model.entity.*;
import com.example.javaecommerce.model.request.CategoryRequest;
import com.example.javaecommerce.model.response.CategoryResponse;
import com.example.javaecommerce.pagination.OffsetPageRequest;
import com.example.javaecommerce.pagination.PaginationPage;
import com.example.javaecommerce.repository.CategoryRepository;
import com.example.javaecommerce.repository.ProductRepository;
import com.example.javaecommerce.service.CategoryService;

import javax.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryResponse> getAllCategories() {
        List<CategoryEntity> categoryEntities = categoryRepository.findAll();
        return categoryMapper.toListCategoryResponse(categoryEntities);
    }

    @Override
    public PaginationPage<CategoryResponse> getAllCategoriesPagination(Integer offset, Integer limited) {
        var pageable = new OffsetPageRequest(offset, limited);
        var categoryList = categoryRepository.findAll(pageable);
        var categoryResponse = categoryList
                .getContent()
                .stream()
                .map(item -> Converter.toModel(item, CategoryResponse.class))
                .collect(Collectors.toList());
        return new PaginationPage<CategoryResponse>()
                .setRecords(categoryResponse)
                .setLimit(categoryList.getSize())
                .setTotalRecords(categoryList.getTotalElements())
                .setOffset(categoryList.getNumber());
    }

    @Override
    public CategoryResponse addCategory(CategoryRequest categoryRequest) {
        CategoryEntity categoryEntity = Converter.toModel(categoryRequest, CategoryEntity.class);
        categoryRepository.save(categoryEntity);
        return categoryMapper.toCategoryResponse(categoryEntity);
    }

    @Override
    public CategoryResponse getCategoryById(Long categoryID) {
        CategoryEntity categoryEntity = categoryRepository.findById(categoryID).orElseThrow(() -> new IllegalStateException(
                "category with id" + " does not exist"
        ));
        return categoryMapper.toCategoryResponse(categoryEntity);
    }

    @Override
    public void deleteCategory(Long categoryID) throws Exception {
        ProductEntity productEntity = productRepository.findAll().stream().filter(
                        pro -> Objects.equals(pro.getCategory().getId(), categoryID))
                .findFirst()
                .orElse(null);
        if (null != productEntity) {
            throw new Exception("product is existed");
        } else {
            categoryRepository.deleteById(categoryID);
        }
    }

    @Override
    public CategoryResponse updateCategory(CategoryRequest categoryRequest, Long id) {
        CategoryEntity categoryEntity = categoryRepository.findById(id)
                .map(category -> {
                    category.setName(categoryRequest.getName());
                    return categoryRepository.save(category);
                }).orElseThrow(() -> new IllegalStateException(
                        "product with id " + id + " does not exist"
                ));
        return categoryMapper.toCategoryResponse(categoryEntity);
    }
}
