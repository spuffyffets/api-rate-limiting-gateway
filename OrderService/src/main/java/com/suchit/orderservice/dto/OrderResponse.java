package com.suchit.orderservice.dto;

import java.time.LocalDateTime;

import com.suchit.orderservice.entity.OrderStatus;

import lombok.Data;

@Data
public class OrderResponse {

    private Long id;

    private String customerName;

    private String productName;

    private Integer quantity;

    private Double totalAmount;

    private OrderStatus status;

    private LocalDateTime createdAt;
}