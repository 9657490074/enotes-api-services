package com.org.enotesapiservice;

import com.org.enotesapiservice.entity.Category;
import com.org.enotesapiservice.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/category")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/saveCategory")
    public ResponseEntity<?> saveCategory(@RequestBody Category category) {
        Boolean saveCategory = categoryService.saveCategory(category);
        if (saveCategory) {
            return new ResponseEntity<>("Saved Success", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Not Saved ! Please try again", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/category")
    public ResponseEntity<?> getAllCategory() {
        List<Category> allCategory = categoryService.getAllCategory();
        if (CollectionUtils.isEmpty(allCategory)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            //return ResponseEntity.noContent().build();   //2 way
        } else {
            return new ResponseEntity<>(allCategory, HttpStatus.OK);
        }
    }

}
