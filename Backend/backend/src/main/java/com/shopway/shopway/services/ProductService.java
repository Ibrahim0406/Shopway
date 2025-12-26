package com.shopway.shopway.services;

import com.shopway.shopway.dto.ProductDto;
import com.shopway.shopway.entities.Product;

import java.util.List;
import java.util.UUID;

public interface ProductService {

    public Product createProduct(ProductDto product);

    public List<ProductDto> getAllProducts(UUID categoryId, UUID typeId);


    ProductDto getProductBySlug(String slug);

    ProductDto getProductById(UUID id);

    Product updateProduct(ProductDto productDto);

    Product fetchProductById(UUID uuid) throws Exception;
}
