package com.suchit.apigateway.ratelimit.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RateLimitPolicy {

    private Integer bucketCapacity;

    private Integer refillRate;
}