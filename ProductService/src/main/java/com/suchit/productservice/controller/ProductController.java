package com.suchit.productservice.controller;

import org.springframework.web.bind.annotation.*;

import com.suchit.productservice.dto.ProductRequest;
import com.suchit.productservice.dto.Response;
import com.suchit.productservice.service.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public Response createProduct(
            @RequestBody ProductRequest productRequest){

        return productService.createProduct(productRequest);
    }

    @GetMapping
    public Response getAllProducts(){

        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public Response getProductById(
            @PathVariable Long id){

        return productService.getProductById(id);
    }

    @PutMapping("/{id}")
    public Response updateProduct(
            @PathVariable Long id,
            @RequestBody ProductRequest productRequest){

        return productService.updateProduct(id, productRequest);
    }

    @DeleteMapping("/{id}")
    public Response deleteProduct(
            @PathVariable Long id){

        return productService.deleteProduct(id);
    }
}