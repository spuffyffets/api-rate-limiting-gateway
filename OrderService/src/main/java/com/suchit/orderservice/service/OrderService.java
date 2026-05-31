package com.suchit.orderservice.service;

import com.suchit.orderservice.dto.OrderRequest;
import com.suchit.orderservice.dto.Response;

public interface OrderService {

    Response createOrder(OrderRequest orderRequest);

    Response getAllOrders();

    Response getOrderById(Long id);

    Response cancelOrder(Long id);
}