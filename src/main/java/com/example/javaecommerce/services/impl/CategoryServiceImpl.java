package com.example.javaecommerce.services.impl;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final CategoryMapper categoryMapper;
    private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Override
    public PaginationPage<CategoryResponse> getAllCategoriesPagination(final Integer offset, final Integer limited) {
        try {
            var pageable = new OffsetPageRequest(offset, limited);
            var categoryList = categoryRepository.findAll(pageable);
            var categoryResponse = categoryList
                    .getContent()
                    .stream()
                    .map(item -> categoryMapper.toCategoryResponse(item))
                    .collect(Collectors.toList());
            logger.info("Get all category with pagination successfully");
            return new PaginationPage<CategoryResponse>()
                    .setRecords(categoryResponse)
                    .setLimit(categoryList.getSize())
                    .setTotalRecords(categoryList.getTotalElements())
                    .setOffset(categoryList.getNumber());
        } catch (Exception e) {
            logger.info("Failed to get all category with pagination", e);
            throw new RuntimeException("Failed to get all category with pagination");
        }
    }

    @Override
    public CategoryResponse addCategory(final CategoryRequest categoryRequest) {
        try {
            CategoryEntity categoryEntity = categoryMapper.toCategoryEntity(categoryRequest);
            categoryRepository.save(categoryEntity);
            logger.info("Create category successfully!");
            return categoryMapper.toCategoryResponse(categoryEntity);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create category", e);
        }
    }

    @Override
    public CategoryResponse getCategoryById(final Long categoryId) {
        try {
            CategoryEntity categoryEntity = categoryRepository
                    .findById(categoryId)
                    .orElseThrow(() -> new EcommerceRunTimeException(ErrorCode.ID_NOT_FOUND));
            logger.info("Get category successfully by id {}", categoryId);
            return categoryMapper.toCategoryResponse(categoryEntity);
        } catch (Exception e) {
            logger.info("Failed to get category by id", e);
            throw new RuntimeException("Failed to get category by id", e);
        }
    }

    @Override
    public void deleteCategory(final Long categoryId) {
        try {
            ProductEntity productEntity = productRepository.findAll()
                    .stream()
                    .filter(
                            pro -> Objects.equals(pro.getCategory().getId(), categoryId))
                    .findFirst()
                    .orElse(null);
            if (null != productEntity) {
                throw new EcommerceRunTimeException(ErrorCode.ITEM_EXISTED);
            } else {
                logger.info("Delete item by category id {} ", categoryId);
                categoryRepository.deleteById(categoryId);
            }
        } catch (Exception e) {
            logger.info("Failed to delete category by id", e);
            throw new RuntimeException("Failed to delete category by id", e);
        }
    }

    @Override
    public CategoryResponse updateCategory(final CategoryRequest categoryRequest, final Long id) {
        try {
            if (StringUtils.isBlank(categoryRequest.getName())) {
                throw new EcommerceRunTimeException(ErrorCode.SHOULD_NOT_BLANK);
            }
            CategoryEntity categoryEntity = categoryRepository.findById(id)
                    .map(category -> {
                        category.setName(categoryRequest.getName());
                        return categoryRepository.save(category);
                    }).orElseThrow(() -> new EcommerceRunTimeException(ErrorCode.ID_NOT_FOUND));
            logger.info("Update category successfully by category id {}", id);
            return categoryMapper.toCategoryResponse(categoryEntity);
        } catch (Exception e) {
            logger.info("Failed to update category", e);
            throw new RuntimeException("Failed to update category");
        }
    }
}
