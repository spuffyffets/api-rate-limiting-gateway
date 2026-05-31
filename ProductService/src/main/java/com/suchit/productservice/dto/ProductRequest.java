package com.suchit.productservice.dto;

import lombok.Data;

@Data
public class ProductRequest {

    private String productName;

    private String description;

    private Double price;

    private Integer quantity;
}