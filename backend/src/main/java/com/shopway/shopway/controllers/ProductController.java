package com.shopway.shopway.controllers;


import com.shopway.shopway.dto.ProductDto;
import com.shopway.shopway.entities.Product;
import com.shopway.shopway.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;


    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts(@RequestParam (required = false, name = "categoryId") UUID categoryId, @RequestParam (required = false) UUID typeId) {
        List<ProductDto> productList = productService.getAllProducts(categoryId, typeId);
        return new ResponseEntity<>(productList, HttpStatus.OK);
    };

    //Pravljenje produkta (kreiranje)
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody ProductDto productDto){
        Product product = productService.createProduct(productDto);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

//    @PostMapping
//    public ResponseEntity<Product> createProduct(@RequestBody ProductDto productDto){
//        Product product = productService.createProduct(productDto);
//        return new ResponseEntity<>(product, HttpStatus.CREATED);
//    }



}
