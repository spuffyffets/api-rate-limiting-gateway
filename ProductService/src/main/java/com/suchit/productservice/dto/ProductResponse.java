package com.suchit.productservice.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductResponse {

    private Long id;

    private String productName;

    private String description;

    private Double price;

    private Integer quantity;

    private LocalDateTime createdAt;
}