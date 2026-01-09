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

/*
 * Mapper klasa za konverziju između Product entiteta i ProductDto objekata.
 * Koristi se za mapiranje podataka između sloja baze i API sloja.
 */
@Component
public class ProductMapper {

    @Autowired
    private CategoryService categoryService;

    /*
     * Mapira ProductDto u Product entitet spreman za čuvanje u bazi.
     *
     * Proces:
     * 1. Kreira osnovni Product objekat sa podacima iz DTO-a
     * 2. Pronalazi i povezuje Category
     * 3. Pronalazi i povezuje CategoryType (validira da tip pripada kategoriji)
     * 4. Mapira listu varijanti (boja/veličina/stanje zaliha)
     * 5. Mapira listu resursa (slike/videi)
     *
     * @param productDto DTO objekat sa podacima proizvoda
     * @return Product entitet spreman za čuvanje
     * @throws IllegalArgumentException ako kategorija ili tip kategorije ne postoje
     */
    public Product mapToProductEntity(ProductDto productDto) {
        Product product = new Product();
        if (null != productDto.getId()){
            product.setId(productDto.getId());
        }
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setBrand(productDto.getBrand());
        product.setNewArrival(productDto.isNewArrival());
        product.setSlug(productDto.getSlug());
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

        return product;
    }

    /*
     * Mapira listu ProductResourceDto u listu Resources entiteta.
     * Svaki resurs dobija referencu na parent Product (bidirekciona veza).
     *
     * @param productResources lista ProductResourceDto objekata
     * @param product parent Product objekat
     * @return lista Resources entiteta
     */
    private List<Resources> mapToProductResources(List<ProductResourceDto> productResources, Product product) {
        return productResources.stream().map(productResourceDto -> {
            Resources resources = new Resources();
            if (null != productResourceDto.getId()){
                resources.setId(productResourceDto.getId());
            }
            resources.setName(productResourceDto.getName());
            resources.setType(productResourceDto.getType());
            resources.setUrl(productResourceDto.getUrl());
            resources.setIsPrimary(productResourceDto.getIsPrimary());
            resources.setProduct(product);
            return resources;
        }).collect(Collectors.toList());
    }

    /*
     * Mapira listu ProductVariantDto u listu ProductVariant entiteta.
     * Svaka varijanta dobija referencu na parent Product (bidirekciona veza).
     *
     * @param productVariantDtos lista ProductVariantDto objekata
     * @param product parent Product objekat
     * @return lista ProductVariant entiteta
     */
    private List<ProductVariant> mapToProductVariant(List<ProductVariantDto> productVariantDtos, Product product){
        return productVariantDtos.stream().map(productVariantDto -> {
            ProductVariant productVariant = new ProductVariant();
            if (null != productVariantDto.getId()){
                productVariant.setId(productVariantDto.getId());
            }
            productVariant.setColor(productVariantDto.getColor());
            productVariant.setSize(productVariantDto.getSize());
            productVariant.setStockQuantity(productVariantDto.getStockQuantity());
            productVariant.setProduct(product);
            return productVariant;
        }).collect(Collectors.toList());
    }

    /*
     * Mapira listu Product entiteta u listu ProductDto objekata (osnovni podaci).
     * Koristi se za listing stranice gde nisu potrebni svi detalji.
     *
     * @param products lista Product entiteta
     * @return lista ProductDto objekata sa osnovnim podacima
     */
    public List<ProductDto> getProductDtos(List<Product> products) {
        return products.stream().map(this::mapProductToDto).toList();
    }

    /*
     * Mapira Product entitet u ProductDto sa osnovnim podacima.
     * Uključuje samo osnovne informacije i thumbnail sliku.
     *
     * @param product Product entitet
     * @return ProductDto sa osnovnim podacima
     */
    public ProductDto mapProductToDto(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .brand(product.getBrand())
                .rating(product.getRating())
                .slug(product.getSlug())
                .thumbnail(getProductThumbnail(product.getResources()))
                .build();

    }

    /*
     * Pronalazi primarnu sliku proizvoda iz liste resursa.
     * Primarna slika se koristi kao thumbnail.
     *
     * @param resources lista Resources objekata
     * @return URL primarne slike
     */
    private String getProductThumbnail(List<Resources> resources) {
        return resources.stream().filter(Resources::getIsPrimary).findFirst().orElse(null).getUrl();
    }

    /*
     * Mapira listu ProductVariant entiteta u listu ProductVariantDto objekata.
     *
     * @param productVariants lista ProductVariant entiteta
     * @return lista ProductVariantDto objekata
     */
    public List<ProductVariantDto> mapProductVariantListToDto(List<ProductVariant> productVariants) {
        return productVariants.stream().map(this::mapProductVariantDto).toList();
    }

    /*
     * Mapira ProductVariant entitet u ProductVariantDto.
     *
     * @param productVariant ProductVariant entitet
     * @return ProductVariantDto objekat
     */
    private ProductVariantDto mapProductVariantDto(ProductVariant productVariant) {
        return ProductVariantDto.builder()
                .id(productVariant.getId())
                .color(productVariant.getColor())
                .size(productVariant.getSize())
                .stockQuantity(productVariant.getStockQuantity())
                .build();
    }

    /*
     * Mapira listu Resources entiteta u listu ProductResourceDto objekata.
     *
     * @param resources lista Resources entiteta
     * @return lista ProductResourceDto objekata
     */
    public List<ProductResourceDto> mapProductResourcesListDto(List<Resources> resources) {
        return resources.stream().map(this::mapResourceToDto).toList();
    }

    /*
     * Mapira Resources entitet u ProductResourceDto.
     *
     * @param resources Resources entitet
     * @return ProductResourceDto objekat
     */
    private ProductResourceDto mapResourceToDto(Resources resources) {
        return ProductResourceDto.builder()
                .id(resources.getId())
                .name(resources.getName())
                .type(resources.getType())
                .url(resources.getUrl())
                .isPrimary(resources.getIsPrimary())
                .build();
    }
}
