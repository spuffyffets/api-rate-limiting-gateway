package com.suchit.orderservice.controller;

import org.springframework.web.bind.annotation.*;

import com.suchit.orderservice.dto.OrderRequest;
import com.suchit.orderservice.dto.Response;
import com.suchit.orderservice.service.OrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public Response createOrder(
            @RequestBody OrderRequest orderRequest) {

        return orderService.createOrder(orderRequest);
    }

    @GetMapping
    public Response getAllOrders() {

        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public Response getOrderById(
            @PathVariable Long id) {

        return orderService.getOrderById(id);
    }

    @PutMapping("/{id}/cancel")
    public Response cancelOrder(
            @PathVariable Long id) {

        return orderService.cancelOrder(id);
    }
}