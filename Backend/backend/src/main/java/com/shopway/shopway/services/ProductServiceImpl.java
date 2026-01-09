package com.shopway.shopway.services;

import com.shopway.shopway.dto.ProductDto;
import com.shopway.shopway.entities.*;
import com.shopway.shopway.exceptions.ResourceNotFoundException;
import com.shopway.shopway.mapper.ProductMapper;
import com.shopway.shopway.repositories.ProductRepository;
import com.shopway.shopway.specification.ProductSpecification;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.UUID;
/*
 * Implementacija ProductService interfejsa za upravljanje proizvodima.
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductMapper productMapper;
    /*
     * Kreira novi proizvod u bazi.
     * Mapira ProductDto u Product entitet i čuva sa svim povezanim objektima
     * (ProductVariant i Resources) zahvaljujući CascadeType.ALL.
     *
     * @param productDto DTO sa podacima proizvoda, varijanti i resursa
     * @return sačuvani Product sa generisanim ID-em
     */
    @Override
    public Product createProduct(ProductDto productDto) {
        Product product = productMapper.mapToProductEntity(productDto);
        return productRepository.save(product);
    }
    /*
     * Vraća listu proizvoda sa opcionalnim filtriranjem po kategoriji i/ili tipu kategorije.
     * Koristi JPA Specification za dinamičko kreiranje upita.
     *
     * @param categoryId opcioni UUID kategorije za filtriranje
     * @param typeId opcioni UUID tipa kategorije za filtriranje
     * @return lista ProductDto objekata (osnovni podaci bez detaljnih varijanti i resursa)
     */
    @Override
    public List<ProductDto> getAllProducts(UUID categoryId, UUID typeId) {

        Specification<Product> productSpecification = (root, query, cb) -> cb.conjunction();

        if (null != categoryId) {
            productSpecification = productSpecification.and(ProductSpecification.hasCategoryId(categoryId));
        }

        if (null != typeId) {
            productSpecification = productSpecification.and(ProductSpecification.hasCategoryTypeId(typeId));
        }

        List<Product> products = productRepository.findAll(productSpecification);
        return productMapper.getProductDtos(products);
    }
    /*
     * Pronalazi proizvod po slug-u (jedinstveni URL-friendly identifikator).
     * Vraća kompletne podatke proizvoda uključujući varijante i resurse.
     *
     * @param slug URL-friendly naziv proizvoda (npr. "nike-air-max-90")
     * @return ProductDto sa svim detaljima
     * @throws ResourceNotFoundException ako proizvod sa datim slug-om ne postoji
     */
    @Override
    public ProductDto getProductBySlug(String slug) {
        Product product = productRepository.findBySlug(slug);
        if (null == product){
            throw new ResourceNotFoundException("Product not found");
        }
        ProductDto productDto = productMapper.mapProductToDto(product);
        productDto.setCategoryId(product.getCategory().getId());
        productDto.setCategoryTypeId(product.getCategoryType().getId());
        productDto.setVariants(productMapper.mapProductVariantListToDto(product.getProductVariants()));
        productDto.setProductResources(productMapper.mapProductResourcesListDto(product.getResources()));
        return productDto;
    }
    /*
     * Pronalazi proizvod po ID-u.
     * Vraća kompletne podatke proizvoda uključujući varijante i resurse.
     *
     * @param id UUID proizvoda
     * @return ProductDto sa svim detaljima
     * @throws ResourceNotFoundException ako proizvod sa datim ID-em ne postoji
     */
    @Override
    public ProductDto getProductById(UUID id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        ProductDto productDto = productMapper.mapProductToDto(product);
        productDto.setCategoryId(product.getCategory().getId());
        productDto.setCategoryTypeId(product.getCategoryType().getId());
        productDto.setVariants(productMapper.mapProductVariantListToDto(product.getProductVariants()));
        productDto.setProductResources(productMapper.mapProductResourcesListDto(product.getResources()));
        return productDto;
    }
    /*
     * Ažurira postojeći proizvod.
     * Prvo proverava da li proizvod postoji, zatim mapira DTO u entitet i čuva promene.
     *
     * @param productDto DTO sa novim podacima (mora sadržati ID)
     * @return ažurirani Product
     * @throws ResourceNotFoundException ako proizvod sa datim ID-em ne postoji
     */
    @Override
    public Product updateProduct(ProductDto productDto) {
        Product product = productRepository.findById(productDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        return productRepository.save(productMapper.mapToProductEntity(productDto));
    }
    /*
     * Pomoćna metoda koja pronalazi proizvod po ID-u.
     * Koristi se interno u OrderService prilikom kreiranja narudžbine.
     *
     * @param id UUID proizvoda
     * @return Product entitet
     * @throws BadRequestException ako proizvod ne postoji
     */
    @Override
    public Product fetchProductById(UUID id) throws Exception {
        return productRepository.findById(id).orElseThrow(BadRequestException::new);
    }
}

