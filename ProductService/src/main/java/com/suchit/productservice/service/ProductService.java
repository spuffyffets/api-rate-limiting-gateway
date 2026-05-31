package com.suchit.productservice.service;

import com.suchit.productservice.dto.ProductRequest;
import com.suchit.productservice.dto.Response;

public interface ProductService {

    Response createProduct(ProductRequest productRequest);

    Response getAllProducts();

    Response getProductById(Long id);

    Response updateProduct(Long id, ProductRequest productRequest);

    Response deleteProduct(Long id);
}