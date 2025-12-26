package com.shopway.shopway.services;

import com.shopway.shopway.dto.CategoryDto;
import com.shopway.shopway.dto.CategoryTypeDto;
import com.shopway.shopway.entities.Category;
import com.shopway.shopway.entities.CategoryType;
import com.shopway.shopway.exceptions.ResourceNotFoundException;
import com.shopway.shopway.repositories.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);

    @Autowired
    private CategoryRepository categoryRepository;

    public Category getCategory(UUID categoryId) {
        Optional<Category> category = categoryRepository.findById(categoryId);
        return category.orElse(null);
    }

    public Category createCategory(CategoryDto categoryDto) {
        logger.info("CategoryService.createCategory pozvan sa categoryDto: {}", categoryDto);
        Category category = mapToEntity(categoryDto);
        logger.info("Mapirani Category entitet, spremam u bazu...");
        Category saved = categoryRepository.save(category);
        logger.info("Spašen Category sa ID: {}, CategoryTypes count: {}",
                saved.getId(), saved.getCategoryTypes() != null ? saved.getCategoryTypes().size() : 0);
        return saved;
    }

    private Category mapToEntity(CategoryDto categoryDto) {
        logger.info("Mapiranje CategoryDto u Category entitet...");
        Category category = Category.builder()
                .code(categoryDto.getCode())
                .name(categoryDto.getName())
                .description(categoryDto.getDescription())
                .build();

        if (null != categoryDto.getCategoryTypeList()) {
            logger.info("CategoryTypeList nije null, ima {} elemenata", categoryDto.getCategoryTypeList().size());
            List<CategoryType> categoryTypes = mapToCategoryTypes(categoryDto.getCategoryTypeList(), category);
            category.setCategoryTypes(categoryTypes);
            logger.info("Postavljeno {} CategoryType-ova na Category", categoryTypes.size());
        } else {
            logger.warn("CategoryTypeList je NULL u CategoryDto!");
        }
        return category;
    }

    private List<CategoryType> mapToCategoryTypes(List<CategoryTypeDto> categoryTypeList, Category category) {
        logger.info("Mapiranje {} CategoryTypeDto objekata...", categoryTypeList.size());
        return categoryTypeList
                .stream()
                .map(categoryTypeDto -> {
                    logger.info("Mapiranje CategoryTypeDto: code={}, name={}, description={}",
                            categoryTypeDto.getCode(), categoryTypeDto.getName(), categoryTypeDto.getDescription());

                    CategoryType categoryType = new CategoryType();
                    categoryType.setCode(categoryTypeDto.getCode());
                    categoryType.setName(categoryTypeDto.getName());
                    categoryType.setDescription(categoryTypeDto.getDescription());
                    categoryType.setCategory(category);

                    logger.info("Kreiran CategoryType: code={}, name={}, description={}",
                            categoryType.getCode(), categoryType.getName(), categoryType.getDescription());
                    return categoryType;
                }).collect(Collectors.toList());
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category updateCategory(CategoryDto categoryDto, UUID categoryId) {
        logger.info("CategoryService.updateCategory pozvan za ID: {}", categoryId);
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryDto.getId()));

        if (null != categoryDto.getName()) {
            category.setName(categoryDto.getName());
        }
        if (null != categoryDto.getCode()) {
            category.setCode(categoryDto.getCode());
        }
        if (null != categoryDto.getDescription()) {
            category.setDescription(categoryDto.getDescription());
        }

        List<CategoryType> existing = category.getCategoryTypes();
        List<CategoryType> list = new ArrayList<>();

        if (categoryDto.getCategoryTypeList() != null) {
            logger.info("Ažuriram CategoryTypes, lista ima {} elemenata", categoryDto.getCategoryTypeList().size());
            categoryDto.getCategoryTypeList().stream().forEach(categoryTypeDto -> {
                logger.info("Obrađujem CategoryTypeDto: id={}, code={}, name={}, description={}",
                        categoryTypeDto.getId(), categoryTypeDto.getCode(), categoryTypeDto.getName(), categoryTypeDto.getDescription());

                if (null != categoryTypeDto.getId()) {
                    Optional<CategoryType> categoryType = existing.stream().filter(t -> t.getId()
                            .equals(categoryTypeDto.getId())).findFirst();

                    CategoryType categoryType1 = categoryType.get();
                    categoryType1.setCode(categoryTypeDto.getCode());
                    categoryType1.setName(categoryTypeDto.getName());
                    categoryType1.setDescription(categoryTypeDto.getDescription());
                    list.add(categoryType1);
                    logger.info("Ažuriran postojeći CategoryType sa ID: {}", categoryType1.getId());
                } else {
                    CategoryType categoryType = new CategoryType();
                    categoryType.setCode(categoryTypeDto.getCode());
                    categoryType.setName(categoryTypeDto.getName());
                    categoryType.setDescription(categoryTypeDto.getDescription());
                    categoryType.setCategory(category);
                    list.add(categoryType);
                    logger.info("Kreiran novi CategoryType: code={}", categoryType.getCode());
                }
            });
        } else {
            logger.warn("CategoryTypeList je NULL!");
        }

        category.setCategoryTypes(list);
        Category saved = categoryRepository.save(category);
        logger.info("Ažuriran Category sa ID: {}, CategoryTypes count: {}",
                saved.getId(), saved.getCategoryTypes() != null ? saved.getCategoryTypes().size() : 0);
        return saved;
    }

    public void deleteCategory(UUID categoryId) {
        categoryRepository.deleteById(categoryId);
    }
}