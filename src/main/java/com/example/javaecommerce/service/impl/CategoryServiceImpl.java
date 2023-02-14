package com.example.javaecommerce.service.impl;

import com.example.javaecommerce.converter.Converter;
import com.example.javaecommerce.model.entity.*;
import com.example.javaecommerce.model.request.CategoryRequest;
import com.example.javaecommerce.model.response.CategoryResponse;
import com.example.javaecommerce.repository.CategoryRepository;
import com.example.javaecommerce.repository.ProductRepository;
import com.example.javaecommerce.service.CategoryService;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {
    private final  CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @Override
    public List<CategoryResponse> getAllCategories() {
        List<CategoryEntity> categoryEntities = categoryRepository.findAll();
        return Converter.toList(categoryEntities, CategoryResponse.class);
    }
    @Override
    public CategoryResponse addCategory(CategoryRequest categoryRequest) {
        CategoryEntity categoryEntity = Converter.toModel(categoryRequest, CategoryEntity.class);
        categoryRepository.save(categoryEntity);
        return Converter.toModel(categoryEntity, CategoryResponse.class);
    }
    @Override
    public CategoryResponse getCategoryById(Long categoryID) {
;          CategoryEntity categoryEntity = categoryRepository.findById(categoryID).orElseThrow(() -> new IllegalStateException(
            "category with id" + " does not exist"
        ));
        return Converter.toModel(categoryEntity, CategoryResponse.class);
    }

    @Override
    public void deleteCategory(Long categoryID) throws Exception {
    ProductEntity productEntity =   productRepository.findAll().stream().filter(                        
       pro -> Objects.equals(pro.getCategory().getId(), categoryID)   )                                
               .findFirst()                                                                            
               .orElse(null);                                                                         
       if (null != productEntity) {                                                                   
           throw new Exception("product is existed");                                                 
       }  else {                                                                                      
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
    return Converter.toModel(categoryEntity, CategoryResponse.class);
    }
}
