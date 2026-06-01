package com.suchit.apigateway.ratelimit.dto;

import lombok.Data;

@Data
public class RateLimitRequest {

    private String apiPath;

    private String role;

    private Integer requestLimit;
}