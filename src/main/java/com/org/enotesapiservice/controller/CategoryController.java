package com.org.enotesapiservice.controller;

import com.org.enotesapiservice.dto.CategoryDto;
import com.org.enotesapiservice.dto.CategoryResponse;
import com.org.enotesapiservice.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/category")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/saveCategory")
    public ResponseEntity<?> saveCategory(@RequestBody CategoryDto categoryDto) {
        Boolean saveCategory = categoryService.saveCategory(categoryDto);
        if (saveCategory) {
            return new ResponseEntity<>("Saved Success", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Not Saved ! Please try again", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/")
    public ResponseEntity<?> getAllCategory() {
        List<CategoryDto> allCategory = categoryService.getAllCategory();
        if (CollectionUtils.isEmpty(allCategory)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            //return ResponseEntity.noContent().build();   //2 way
        } else {
            return new ResponseEntity<>(allCategory, HttpStatus.OK);
        }
    }

    @GetMapping("/active")
    public ResponseEntity<?> getActiveCategory() {
        List<CategoryResponse> allCategory = categoryService.getActiveCategory();
        if (CollectionUtils.isEmpty(allCategory)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            //return ResponseEntity.noContent().build();   //2 way
        } else {
            return new ResponseEntity<>(allCategory, HttpStatus.OK);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryDetailsById(@PathVariable Integer id) {
        CategoryDto categoryDto = categoryService.getCategoryById(id);
        if (ObjectUtils.isEmpty(categoryDto)) {
            return new ResponseEntity<>("Category not found with id:" + id, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(categoryDto, HttpStatus.OK);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategoryById(@PathVariable Integer id) {
        Boolean deleted = categoryService.deleteCategoryById(id);
        if (deleted) {
            return new ResponseEntity<>("Category delete successfully:", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Category not delete successfully:", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
