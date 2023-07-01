package com.example.javaecommerce.services.impl;

import com.example.javaecommerce.converter.Converter;
import com.example.javaecommerce.exception.EcommerceRunTimeException;
import com.example.javaecommerce.exception.ErrorCode;
import com.example.javaecommerce.mapper.CategoryMapper;
import com.example.javaecommerce.model.entity.*;
import com.example.javaecommerce.model.request.CategoryRequest;
import com.example.javaecommerce.model.response.CategoryResponse;
import com.example.javaecommerce.pagination.OffsetPageRequest;
import com.example.javaecommerce.pagination.PaginationPage;
import com.example.javaecommerce.repository.CategoryRepository;
import com.example.javaecommerce.repository.ProductRepository;
import com.example.javaecommerce.services.CategoryService;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
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
    public PaginationPage<CategoryResponse> getAllCategoriesPagination(final Integer offset, final Integer limited) {
        var pageable = new OffsetPageRequest(offset, limited);
        var categoryList = categoryRepository.findAll(pageable);
        var categoryResponse = categoryList
                .getContent()
                .stream()
                .map(item -> categoryMapper.toCategoryResponse(item))
                .collect(Collectors.toList());
        return new PaginationPage<CategoryResponse>()
                .setRecords(categoryResponse)
                .setLimit(categoryList.getSize())
                .setTotalRecords(categoryList.getTotalElements())
                .setOffset(categoryList.getNumber());
    }
    @Override
    public CategoryResponse addCategory(final CategoryRequest categoryRequest) {
        CategoryEntity categoryEntity = Converter.toModel(categoryRequest, CategoryEntity.class);
        categoryRepository.save(categoryEntity);
        return categoryMapper.toCategoryResponse(categoryEntity);
    }
    @Override
    public CategoryResponse getCategoryById(final Long categoryId) {
        CategoryEntity categoryEntity = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EcommerceRunTimeException(ErrorCode.ID_NOT_FOUND));
        return categoryMapper.toCategoryResponse(categoryEntity);
    }

    @Override
    public void deleteCategory(final Long categoryId) {
        ProductEntity productEntity = productRepository.findAll()
                .stream()
                .filter(
                        pro -> Objects.equals(pro.getCategory().getId(), categoryId))
                .findFirst()
                .orElse(null);
        if (null != productEntity) {
            throw new EcommerceRunTimeException(ErrorCode.ITEM_EXISTED);
        } else {
            categoryRepository.deleteById(categoryId);
        }
    }

    @Override
    public CategoryResponse updateCategory(final CategoryRequest categoryRequest, final Long id) {
        if (StringUtils.isBlank(categoryRequest.getName())) {
            throw new EcommerceRunTimeException(ErrorCode.SHOULD_NOT_BLANK);
        }
        CategoryEntity categoryEntity = categoryRepository.findById(id)
                .map(category -> {
                    category.setName(categoryRequest.getName());
                    return categoryRepository.save(category);
                }).orElseThrow(() -> new EcommerceRunTimeException(ErrorCode.ID_NOT_FOUND));
        return categoryMapper.toCategoryResponse(categoryEntity);
    }
}
