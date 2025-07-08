package com.org.enotesapiservice.service;

import com.org.enotesapiservice.entity.Category;

import java.util.List;

public interface CategoryService {

    Boolean saveCategory(Category category);

    List<Category> getAllCategory();

}
