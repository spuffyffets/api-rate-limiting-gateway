package com.suchit.apigateway.ratelimit;

import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.suchit.apigateway.util.JwtUtils;

import reactor.core.publisher.Mono;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;

@Component
public class RateLimitFilter implements GlobalFilter, Ordered {

	private final RateLimitService rateLimitService;
	private final JwtUtils jwtUtils;

	public RateLimitFilter(RateLimitService rateLimitService, JwtUtils jwtUtils) {

		this.rateLimitService = rateLimitService;
		this.jwtUtils = jwtUtils;
	}

//	@Override
//	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//
//		String path = exchange.getRequest().getURI().getPath();
//
//		// Skip Auth APIs
//		if (path.startsWith("/api/auth")) {
//			return chain.filter(exchange);
//		}
//
//		String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
//
//		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//
//			return chain.filter(exchange);
//		}
//
//		String token = authHeader.substring(7);
//
//		String email = jwtUtils.getUsernameFromToken(token);
//
//		String redisKey = "rate_limit:" + email;
//
//		return rateLimitService.isAllowed(redisKey).flatMap(allowed -> {
//
//			if (!allowed) {
//
//				exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
//
//				return exchange.getResponse().setComplete();
//			}
//
//			return chain.filter(exchange);
//		});
//	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

		String path = exchange.getRequest().getURI().getPath();

		String method = exchange.getRequest().getMethod().name();

		// test karay sathi fkt

		System.out.println("Method = " + method);

		String redisKey;
		int limit;

		// Login API Rate Limit
		if (path.equals("/api/auth/login")) {

			redisKey = "login_limit";
			limit = 5;

		}
		// Register API Rate Limit
		else if (path.equals("/api/auth/register")) {

			redisKey = "register_limit";
			limit = 3;

		}
		// Protected APIs
		else {

			String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

			if (authHeader == null || !authHeader.startsWith("Bearer ")) {

				return chain.filter(exchange);
			}

			String token = authHeader.substring(7);

			String email = jwtUtils.getUsernameFromToken(token);

			String role = jwtUtils.getRoleFromToken(token);

//			System.out.println("Email = " + email);
//			System.out.println("Role = " + role);

			redisKey = "rate_limit:" + email + ":" + method + ":" + path;
			System.out.println(redisKey);
			
			
//			if ("ADMIN".equals(role)) {
//
//				limit = 100;
//
//			} else {
//
//				limit = 50;
//			}
			
			if (path.startsWith("/api/products")) {

			    if ("ADMIN".equals(role)) {

			        limit = 100;

			    } else {

			        limit = 50;
			    }

			} else if (path.startsWith("/api/orders")) {

			    if ("ADMIN".equals(role)) {

			        limit = 200;

			    } else {

			        limit = 75;
			    }

			} else {

			    limit = 10;
			}
//			System.out.println("Limit = " + limit);
		}

		return rateLimitService.isAllowed(redisKey, limit).flatMap(allowed -> {

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