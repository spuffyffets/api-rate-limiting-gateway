package com.suchit.apigateway.ratelimit;

import org.springframework.core.Ordered;


import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.suchit.apigateway.ratelimit.dto.RateLimitPolicy;
import com.suchit.apigateway.ratelimit.service.RateLimitConfigService;
import com.suchit.apigateway.util.JwtUtils;

import reactor.core.publisher.Mono;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;

@Component
public class RateLimitFilter implements GlobalFilter, Ordered {

	private final RateLimitService rateLimitService;
	private final JwtUtils jwtUtils;
	private final RateLimitConfigService rateLimitConfigService;

	public RateLimitFilter(RateLimitService rateLimitService, JwtUtils jwtUtils,
			RateLimitConfigService rateLimitConfigService) {

		this.rateLimitService = rateLimitService;
		this.jwtUtils = jwtUtils;
		this.rateLimitConfigService = rateLimitConfigService;
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

//		System.out.println("JWT FILTER EXECUTED");

		String path = exchange.getRequest().getURI().getPath();

		String method = exchange.getRequest().getMethod().name();

		String redisKey;

		int capacity;
		int refillRate;

		// Login API
		if (path.equals("/api/auth/login")) {

			redisKey = "login_limit";

			capacity = 5;
			refillRate = 1;
		}

		// Register API
		else if (path.equals("/api/auth/register")) {

			redisKey = "register_limit";

			capacity = 3;
			refillRate = 1;
		}

		// Protected APIs
		else {

			String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

			if (authHeader == null || !authHeader.startsWith("Bearer ")) {

				exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);

				return exchange.getResponse().setComplete();
			}

			String token = authHeader.substring(7);

			String email = jwtUtils.getUsernameFromToken(token);

			String role = jwtUtils.getRoleFromToken(token);

			// Only ADMIN can manage limits
			if (path.startsWith("/api/rate-limit")) {

				if (!"ADMIN".equals(role)) {

					exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);

					return exchange.getResponse().setComplete();
				}
			}

			redisKey = "rate_limit:" + email + ":" + method + ":" + path;

			RateLimitPolicy policy = rateLimitConfigService.getPolicy(path, role);

			capacity = policy.getBucketCapacity();

			refillRate = policy.getRefillRate();

//			System.out.println("Capacity = " + capacity);
//
//			System.out.println("Refill Rate = " + refillRate);
		}

		return rateLimitService.isAllowed(redisKey, capacity, refillRate).flatMap(allowed -> {

			if (!allowed) {

				exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);

				return exchange.getResponse().setComplete();
			}

			return chain.filter(exchange);
		});
	}

	@Override
	public int getOrder() {

		return -2;
	}
}