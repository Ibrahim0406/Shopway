package com.shopway.shopway.controllers;

import com.shopway.shopway.dto.CategoryDto;
import com.shopway.shopway.entities.Category;
import com.shopway.shopway.services.CategoryService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/category")
@CrossOrigin
public class CategoryController {

    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable(value = "id", required = true) UUID categoryId){
        Category category = categoryService.getCategory(categoryId);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories(){
        List<Category> categoryList = categoryService.getAllCategories();
        return new ResponseEntity<>(categoryList, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Category> createCategory(@Valid @RequestBody CategoryDto categoryDto){
        logger.info("POST /api/category - Primljen CategoryDto: code={}, name={}, description={}",
                categoryDto.getCode(), categoryDto.getName(), categoryDto.getDescription());

        if (categoryDto.getCategoryTypeList() != null) {
            logger.info("CategoryTypeList size: {}", categoryDto.getCategoryTypeList().size());
            categoryDto.getCategoryTypeList().forEach(ct ->
                    logger.info("  CategoryType: code={}, name={}, description={}", ct.getCode(), ct.getName(), ct.getDescription())
            );
        } else {
            logger.warn("CategoryTypeList je NULL!");
        }

        Category category = categoryService.createCategory(categoryDto);
        logger.info("Kreirana kategorija sa ID: {}", category.getId());
        return new ResponseEntity<>(category, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@Valid @RequestBody CategoryDto categoryDto, @PathVariable(value = "id", required = true) UUID categoryId){
        logger.info("PUT /api/category/{} - Primljen CategoryDto: code={}, name={}, description={}",
                categoryId, categoryDto.getCode(), categoryDto.getName(), categoryDto.getDescription());

        if (categoryDto.getCategoryTypeList() != null) {
            logger.info("CategoryTypeList size: {}", categoryDto.getCategoryTypeList().size());
            categoryDto.getCategoryTypeList().forEach(ct ->
                    logger.info("  CategoryType: id={}, code={}, name={}, description={}", ct.getId(), ct.getCode(), ct.getName(), ct.getDescription())
            );
        } else {
            logger.warn("CategoryTypeList je NULL!");
        }

        Category updatedCategory = categoryService.updateCategory(categoryDto, categoryId);
        logger.info("Ažurirana kategorija sa ID: {}", updatedCategory.getId());
        return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable(value = "id", required = true) UUID categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.ok().build();
    }

}
