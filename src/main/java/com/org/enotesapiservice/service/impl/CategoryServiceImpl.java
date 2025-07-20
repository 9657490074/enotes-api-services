package com.org.enotesapiservice.service.impl;

import com.org.enotesapiservice.dto.CategoryDto;
import com.org.enotesapiservice.dto.CategoryResponse;
import com.org.enotesapiservice.entity.Category;
import com.org.enotesapiservice.exception.ExistDataException;
import com.org.enotesapiservice.exception.ResourceNotFoundException;
import com.org.enotesapiservice.repository.CategoryRepository;
import com.org.enotesapiservice.service.CategoryService;
import com.org.enotesapiservice.util.Validation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final Validation validation;

    @Override
    public Boolean saveCategory(CategoryDto categoryDto) {

        //validation checking
        validation.categoryValidation(categoryDto);

        //check category existing or not
        Boolean exit = categoryRepository.existsByName(categoryDto.getName().trim());
        if (exit) {
            throw new ExistDataException("Category name already exists:");
        }

        Category category = modelMapper.map(categoryDto, Category.class);
        if (ObjectUtils.isEmpty(category.getId())) {
            category.setIsDeleted(false);
//            category.setCreatedBy(1);
            category.setCreatedOn(new Date());
        } else {
            updateCategory(category);
        }
        Category saveCategory = categoryRepository.save(category);
        return !ObjectUtils.isEmpty(saveCategory);
    }

    private void updateCategory(Category category) {
        Optional<Category> findById = categoryRepository.findById(category.getId());
        if (findById.isPresent()) {
            Category existCategory = findById.get();
            category.setCreatedBy(existCategory.getCreatedBy());
            category.setCreatedOn(existCategory.getCreatedOn());
            category.setIsDeleted(existCategory.getIsDeleted());

//            category.setUpdatedBy(1);
//            category.setUpdatedOn(new Date());
        }
    }

    @Override
    public List<CategoryDto> getAllCategory() {
        List<Category> categories = categoryRepository.findByIsDeletedFalse();
        return categories.stream().map(category -> modelMapper.map(category, CategoryDto.class)).toList();
    }

    @Override
    public List<CategoryResponse> getActiveCategory() {
        List<Category> categories = categoryRepository.findByIsActiveTrueAndIsDeletedFalse();
        return categories.stream().map(category -> modelMapper.map(category, CategoryResponse.class)).toList();

    }

    @Override
    public CategoryDto getCategoryById(Integer id) {
        Category findByCategoryId = categoryRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id:" + id));
        return ObjectUtils.isEmpty(findByCategoryId) ? null : modelMapper.map(findByCategoryId, CategoryDto.class);
    }

    @Override
    public Boolean deleteCategoryById(Integer id) {
        Optional<Category> findByCategoryId = categoryRepository.findById(id);
        if (findByCategoryId.isPresent()) {
            Category category = findByCategoryId.get();
            category.setIsDeleted(true);
            categoryRepository.save(category);
            return true;
        }
        return false;
    }
}
