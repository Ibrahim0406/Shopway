package com.shopway.shopway.mapper;

import com.shopway.shopway.dto.ProductDto;
import com.shopway.shopway.dto.ProductResourceDto;
import com.shopway.shopway.dto.ProductVariantDto;
import com.shopway.shopway.entities.*;
import com.shopway.shopway.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class ProductMapper {

    @Autowired
    private CategoryService categoryService;

    public Product mapToProductEntity(ProductDto productDto) {
        Product product = new Product();
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setBrand(productDto.getBrand());
        product.setNewArrival(productDto.isNewArrival());
        //product.setIsNewArrival(productDto.getIsNewArrival() != null ? productDto.getIsNewArrival() : false);
        product.setPrice(productDto.getPrice());
        product.setRating(productDto.getRating());

        Category category = categoryService.getCategory(productDto.getCategoryId());
        if (category == null) {
            throw new IllegalArgumentException("Category with ID " + productDto.getCategoryId() + " not found");
        }

        product.setCategory(category);

        UUID categoryTypeId = productDto.getCategoryTypeId();
        CategoryType categoryType = category.getCategoryTypes().stream()
                .filter(t -> t.getId().equals(categoryTypeId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("CategoryType with ID " + categoryTypeId + " not found"));

        product.setCategoryType(categoryType);

        if (productDto.getVariants() != null) {
            product.setProductVariants(mapToProductVariant(productDto.getVariants(), product));
        }

        if (productDto.getProductResources() != null) {
            product.setResources(mapToProductResources(productDto.getProductResources(), product));
        }

        return product;  // UKLONI productRepository.save() odavde!
    }

    private List<Resources> mapToProductResources(List<ProductResourceDto> productResources, Product product) {
        return productResources.stream().map(productResourceDto -> {
            Resources resources = new Resources();
            resources.setName(productResourceDto.getName());
            resources.setType(productResourceDto.getType());
            resources.setUrl(productResourceDto.getUrl());
            resources.setIsPrimary(productResourceDto.getIsPrimary());
            resources.setProduct(product);
            return resources;
        }).collect(Collectors.toList());
    }

    private List<ProductVariant> mapToProductVariant(List<ProductVariantDto> productVariantDtos, Product product){
        return productVariantDtos.stream().map(productVariantDto -> {
            ProductVariant productVariant = new ProductVariant();
            productVariant.setColor(productVariantDto.getColor());
            productVariant.setSize(productVariantDto.getSize());
            productVariant.setStockQuantity(productVariantDto.getStockQuantity());
            productVariant.setProduct(product);
            return productVariant;
        }).collect(Collectors.toList());
    }


    public List<ProductDto> getProductDtos(List<Product> products) {
        return products.stream().map(this::mapProductToDto).toList();
    }

    private ProductDto mapProductToDto(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .brand(product.getBrand())
                .rating(product.getRating())
                .thumbnail(getProductThumbnail(product.getResources())).build();

    }

    private String getProductThumbnail(List<Resources> resources) {
        return resources.stream().filter(Resources::getIsPrimary).findFirst().orElse(null).getUrl();
    }
}
