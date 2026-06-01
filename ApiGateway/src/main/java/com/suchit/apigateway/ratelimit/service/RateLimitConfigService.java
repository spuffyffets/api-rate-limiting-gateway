package com.suchit.apigateway.ratelimit.service;

import com.suchit.apigateway.ratelimit.dto.RateLimitRequest;
import com.suchit.apigateway.ratelimit.dto.Response;

public interface RateLimitConfigService {

	Integer getLimit(String apiPath, String role);

	Response addRateLimit(RateLimitRequest request);

	Response getAllRateLimits();

	Response updateRateLimit(Long id, RateLimitRequest request);

	Response deleteRateLimit(Long id);
}