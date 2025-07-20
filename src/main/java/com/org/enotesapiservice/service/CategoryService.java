package com.org.enotesapiservice.service;

import com.org.enotesapiservice.dto.CategoryDto;
import com.org.enotesapiservice.dto.CategoryResponse;
import com.org.enotesapiservice.entity.Category;

import java.util.List;

public interface CategoryService {

    Boolean saveCategory(CategoryDto categoryDto);

    List<CategoryDto> getAllCategory();

    List<CategoryResponse> getActiveCategory();

    CategoryDto getCategoryById(Integer id);

    Boolean deleteCategoryById(Integer id);
}
