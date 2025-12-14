package com.shopway.shopway.services;

import com.shopway.shopway.dto.ProductDto;
import com.shopway.shopway.entities.Product;

import java.util.List;

public interface ProductService {

    public Product addProduct(ProductDto product);

    public List<Product> getAllProducts();


}
