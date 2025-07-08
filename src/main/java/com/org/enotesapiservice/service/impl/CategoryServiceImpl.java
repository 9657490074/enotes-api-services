package com.org.enotesapiservice.service.impl;

import com.org.enotesapiservice.entity.Category;
import com.org.enotesapiservice.repository.CategoryRepository;
import com.org.enotesapiservice.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public Boolean saveCategory(Category category) {
        category.setIsDeleted(false);
        category.setCreatedBy(1);
        category.setCreatedOn(new Date());
        Category saveCategory = categoryRepository.save(category);
        return !ObjectUtils.isEmpty(saveCategory);
    }

    @Override
    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }
}
