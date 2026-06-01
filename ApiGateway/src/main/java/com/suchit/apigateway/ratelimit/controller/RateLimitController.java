package com.suchit.apigateway.ratelimit.controller;

import org.springframework.web.bind.annotation.*;

import com.suchit.apigateway.ratelimit.dto.RateLimitRequest;
import com.suchit.apigateway.ratelimit.dto.Response;
import com.suchit.apigateway.ratelimit.service.RateLimitConfigService;

@RestController
@RequestMapping("/api/rate-limit")
public class RateLimitController {

	private final RateLimitConfigService rateLimitConfigService;

	public RateLimitController(RateLimitConfigService rateLimitConfigService) {

		this.rateLimitConfigService = rateLimitConfigService;
	}

	@PostMapping
	public Response addRateLimit(@RequestBody RateLimitRequest request) {

		return rateLimitConfigService.addRateLimit(request);
	}

	@GetMapping
	public Response getAllRateLimits() {

		return rateLimitConfigService.getAllRateLimits();
	}

	@PutMapping("/{id}")
	public Response updateRateLimit(@PathVariable Long id, @RequestBody RateLimitRequest request) {

		return rateLimitConfigService.updateRateLimit(id, request);
	}

	@DeleteMapping("/{id}")
	public Response deleteRateLimit(@PathVariable Long id) {

		return rateLimitConfigService.deleteRateLimit(id);
	}
}