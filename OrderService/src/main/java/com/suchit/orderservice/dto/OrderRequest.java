package com.suchit.orderservice.dto;

import lombok.Data;

@Data
public class OrderRequest {

    private String customerName;

    private String productName;

    private Integer quantity;

    private Double totalAmount;
}