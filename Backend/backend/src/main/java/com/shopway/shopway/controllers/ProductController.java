package com.shopway.shopway.controllers;



import com.shopway.shopway.dto.ProductDto;
import com.shopway.shopway.entities.Product;
import com.shopway.shopway.services.ProductService;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/*
 * REST kontroler za upravljanje proizvodima.
 */
@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "http://localhost:5137")
public class ProductController {

    private final ProductService productService;


    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /*
     * Vraća listu proizvoda sa opcijama filtriranja.
     * Može da filtrira po kategoriji, tipu kategorije ili slug-u (jedinstveni URL identifikator).
     * Postavlja Content-Range header za React Admin paginaciju.
     *
     * @param categoryId opcioni UUID kategorije za filtriranje
     * @param typeId opcioni UUID tipa kategorije za filtriranje
     * @param slug opcioni slug proizvoda (ako je postavljen, vraća samo taj jedan proizvod)
     * @param response HTTP response za postavljanje headera
     * @return ResponseEntity sa listom ProductDto objekata i HTTP statusom 200 OK
     */
    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts(@RequestParam (required = false, name = "categoryId", value = "categoryId") UUID categoryId, @RequestParam (required = false, name = "typeId", value = "typeId") UUID typeId, @RequestParam (required = false) String slug, HttpServletResponse response) {
        List<ProductDto> productList = new ArrayList<>();
       if (StringUtils.isNotBlank(slug)){
           ProductDto productDto = productService.getProductBySlug(slug);
           productList.add(productDto);
       }else{
           productList = productService.getAllProducts(categoryId, typeId);
       }
        response.setHeader("Content-Range", String.valueOf(productList.size()));
        return new ResponseEntity<>(productList, HttpStatus.OK);
    }

    /*
     * Vraća pojedinačni proizvod po ID-u sa svim detaljima
     * (varijante, resursi, kategorija, itd.).
     *
     * @param id UUID proizvoda
     * @return ResponseEntity sa ProductDto objektom i HTTP statusom 200 OK
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable UUID id) {
        ProductDto productDto = productService.getProductById(id);
        return new ResponseEntity<>(productDto, HttpStatus.OK);
    }

    /*
     * Kreira novi proizvod u sistemu.
     * Automatski kreira povezane varijante (boja/veličina) i resurse (slike).
     *
     * @param productDto DTO objekat sa podacima proizvoda
     * @return ResponseEntity sa kreiranim Product objektom i HTTP statusom 201 CREATED
     */
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody ProductDto productDto){
        Product product = productService.createProduct(productDto);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    /*
     * Ažurira postojeći proizvod.
     * Može ažurirati osnovne podatke, varijante i resurse.
     *
     * @param productDto DTO sa novim podacima (mora sadržati ID)
     * @param id UUID proizvoda koji se ažurira
     * @return ResponseEntity sa ažuriranim Product objektom i HTTP statusom 200 OK
     */
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@RequestBody ProductDto productDto, @PathVariable UUID id){
        Product product = productService.updateProduct(productDto);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }
}
