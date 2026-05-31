package com.suchit.orderservice.service;

import java.time.LocalDateTime;



import org.springframework.stereotype.Service;

import com.suchit.orderservice.dto.OrderRequest;
import com.suchit.orderservice.dto.Response;
import com.suchit.orderservice.entity.Order;
import com.suchit.orderservice.entity.OrderStatus;
import com.suchit.orderservice.exception.NotFoundException;
import com.suchit.orderservice.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Override
    public Response createOrder(OrderRequest orderRequest) {

        Order order = Order.builder()
                .customerName(orderRequest.getCustomerName())
                .productName(orderRequest.getProductName())
                .quantity(orderRequest.getQuantity())
                .totalAmount(orderRequest.getTotalAmount())
                .status(OrderStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        orderRepository.save(order);

        return Response.builder()
                .status(200)
                .message("Order created successfully")
                .build();
    }

    @Override
    public Response getAllOrders() {

        return Response.builder()
                .status(200)
                .message("Orders fetched successfully")
                .data(orderRepository.findAll())
                .build();
    }

    @Override
    public Response getOrderById(Long id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Order not found"));

        return Response.builder()
                .status(200)
                .message("Order fetched successfully")
                .data(order)
                .build();
    }

    @Override
    public Response cancelOrder(Long id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Order not found"));

        order.setStatus(OrderStatus.CANCELLED);

        orderRepository.save(order);

        return Response.builder()
                .status(200)
                .message("Order cancelled successfully")
                .build();
    }
}