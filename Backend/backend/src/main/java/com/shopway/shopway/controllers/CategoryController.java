package com.shopway.shopway.controllers;

import com.shopway.shopway.dto.CategoryDto;
import com.shopway.shopway.entities.Category;
import com.shopway.shopway.services.CategoryService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.UUID;


/*
 * REST kontroler za upravljanje kategorijama proizvoda.
 */
@RestController
@RequestMapping("/api/category")
@CrossOrigin(origins = "http://localhost:5137")
public class CategoryController {

    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private CategoryService categoryService;

    /*
     * Vraća pojedinačnu kategoriju po ID-u.
     *
     * @param categoryId UUID kategorije
     * @return ResponseEntity sa Category objektom i HTTP statusom 200 OK
     */
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable(value = "id", required = true) UUID categoryId){
        Category category = categoryService.getCategory(categoryId);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    /*
     * Vraća sve kategorije iz baze.
     * Dodaje Content-Range header za paginaciju (za React Admin).
     *
     * @param response HTTP response objekat za postavljanje headera
     * @return ResponseEntity sa listom svih kategorija i HTTP statusom 200 OK
     */
    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories(HttpServletResponse response){
        List<Category> categoryList = categoryService.getAllCategories();
        response.setHeader("Content-Range", "category 0-" + (categoryList.size() - 1) + "/" + categoryList.size());
        return new ResponseEntity<>(categoryList, HttpStatus.OK);
    }

    /*
     * Kreira novu kategoriju sa njenim tipovima (subcategorijama).
     * Loguje primljene podatke za debugging.
     *
     * @param categoryDto DTO objekat sa podacima kategorije (code, name, description, categoryTypeList)
     * @return ResponseEntity sa kreiranom Category i HTTP statusom 201 CREATED
     */
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

    /*
     * Ažurira postojeću kategoriju.
     * Omogućava izmenu osnovnih podataka kategorije i dodavanje/izmenu njenih tipova.
     *
     * @param categoryDto DTO sa novim podacima
     * @param categoryId UUID kategorije koja se ažurira
     * @return ResponseEntity sa ažuriranom Category i HTTP statusom 200 OK
     */
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
    /*
     * Briše kategoriju iz baze.
     *
     * @param categoryId UUID kategorije koja se briše
     * @return ResponseEntity sa HTTP statusom 200 OK
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable(value = "id", required = true) UUID categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.ok().build();
    }

}
