package com.suchit.productservice.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.suchit.productservice.dto.ProductRequest;
import com.suchit.productservice.dto.ProductResponse;
import com.suchit.productservice.dto.Response;
import com.suchit.productservice.entity.Product;
import com.suchit.productservice.exception.NotFoundException;
import com.suchit.productservice.exception.ProductAlreadyExistsException;
import com.suchit.productservice.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public Response createProduct(ProductRequest productRequest) {

        if(productRepository.existsByProductName(
                productRequest.getProductName())) {

            throw new ProductAlreadyExistsException(
                    "Product already exists");
        }

        Product product = Product.builder()
                .productName(productRequest.getProductName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .quantity(productRequest.getQuantity())
                .createdAt(LocalDateTime.now())
                .build();

        productRepository.save(product);

        return Response.builder()
                .status(200)
                .message("Product created successfully")
                .build();
    }

    @Override
    public Response getAllProducts() {

        List<ProductResponse> products =
                productRepository.findAll()
                        .stream()
                        .map(this::convertToResponse)
                        .collect(Collectors.toList());

        return Response.builder()
                .status(200)
                .message("Products fetched successfully")
                .data(products)
                .build();
    }

    @Override
    public Response getProductById(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Product not found"));

        return Response.builder()
                .status(200)
                .message("Product fetched successfully")
                .data(convertToResponse(product))
                .build();
    }

    @Override
    public Response updateProduct(Long id,
                                  ProductRequest productRequest) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Product not found"));

        product.setProductName(productRequest.getProductName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setQuantity(productRequest.getQuantity());

        productRepository.save(product);

        return Response.builder()
                .status(200)
                .message("Product updated successfully")
                .build();
    }

    @Override
    public Response deleteProduct(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Product not found"));

        productRepository.delete(product);

        return Response.builder()
                .status(200)
                .message("Product deleted successfully")
                .build();
    }

    private ProductResponse convertToResponse(Product product){

        return ProductResponse.builder()
                .id(product.getId())
                .productName(product.getProductName())
                .description(product.getDescription())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .createdAt(product.getCreatedAt())
                .build();
    }
}