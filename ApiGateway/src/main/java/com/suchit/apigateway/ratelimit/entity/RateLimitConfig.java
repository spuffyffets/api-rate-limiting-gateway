package com.suchit.apigateway.ratelimit.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "rate_limit_config")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RateLimitConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String apiPath;

    private String role;

    private Integer bucketCapacity;

    private Integer refillRate;
}