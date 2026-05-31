package com.suchit.apigateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.suchit.apigateway.util.JwtUtils;

import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

	private final JwtUtils jwtUtils;

	public JwtAuthenticationFilter(JwtUtils jwtUtils) {

		this.jwtUtils = jwtUtils;
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

		System.out.println("JWT FILTER EXECUTED");
		String path = exchange.getRequest().getURI().getPath();

		// Public APIs
		if (path.startsWith("/api/auth")) {

			return chain.filter(exchange);
		}

		String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {

			exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);

			return exchange.getResponse().setComplete();
		}

		String token = authHeader.substring(7);

		if (!jwtUtils.isTokenValid(token)) {

			exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);

			return exchange.getResponse().setComplete();
		}
		String role = jwtUtils.getRoleFromToken(token);

		String method =
		        exchange.getRequest()
		                .getMethod()
		                .name();

		
		if ("MANAGER".equals(role)) {

		    if ("POST".equals(method)
		            || "PUT".equals(method)
		            || "DELETE".equals(method)) {

		        exchange.getResponse()
		                .setStatusCode(HttpStatus.FORBIDDEN);

		        return exchange.getResponse().setComplete();
		    }
		}

		return chain.filter(exchange);
	}

	@Override
	public int getOrder() {

		return -1;
	}
}