package com.org.enotesapiservice.controller;

import com.org.enotesapiservice.dto.CategoryDto;
import com.org.enotesapiservice.dto.CategoryResponse;
import com.org.enotesapiservice.service.CategoryService;
import com.org.enotesapiservice.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/category")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/saveCategory")
    public ResponseEntity<?> saveCategory(@RequestBody CategoryDto categoryDto) {
        Boolean saveCategory = categoryService.saveCategory(categoryDto);
        if (saveCategory) {
            return CommonUtil.createBuildResponseMessage("Category saved successfully", HttpStatus.CREATED);
        } else {
            return CommonUtil.createErrorResponseMessage("Category not saved", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/")
    public ResponseEntity<?> getAllCategory() {
        List<CategoryDto> allCategory = categoryService.getAllCategory();
        if (CollectionUtils.isEmpty(allCategory)) {

            return CommonUtil.createErrorResponseMessage("NO-CONTENT", HttpStatus.NO_CONTENT);
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        } else {
            return CommonUtil.createBuildResponse(allCategory, HttpStatus.OK);
        }
    }

    @GetMapping("/active")
    public ResponseEntity<?> getActiveCategory() {
        List<CategoryResponse> allCategory = categoryService.getActiveCategory();
        if (CollectionUtils.isEmpty(allCategory)) {

            return CommonUtil.createErrorResponseMessage("NO-CONTENT", HttpStatus.NO_CONTENT);
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        } else {
            return CommonUtil.createBuildResponse(allCategory, HttpStatus.OK);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryDetailsById(@PathVariable Integer id) {
        CategoryDto categoryDto = categoryService.getCategoryById(id);
        if (ObjectUtils.isEmpty(categoryDto)) {
            return CommonUtil.createErrorResponseMessage("INTERNAL SERVER ERROR", HttpStatus.NOT_FOUND);
        } else {
            return CommonUtil.createBuildResponse(categoryDto, HttpStatus.OK);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategoryById(@PathVariable Integer id) {
        Boolean deleted = categoryService.deleteCategoryById(id);
        if (deleted) {
            return CommonUtil.createBuildResponse("Category delete successfully:", HttpStatus.OK);
        } else {
            return CommonUtil.createErrorResponseMessage("Category not delete successfully:", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
