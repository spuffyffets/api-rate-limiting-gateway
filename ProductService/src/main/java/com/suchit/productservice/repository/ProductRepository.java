package com.suchit.productservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.suchit.productservice.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsByProductName(String productName);
}