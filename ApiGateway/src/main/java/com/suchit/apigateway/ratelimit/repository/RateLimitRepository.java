package com.suchit.apigateway.ratelimit.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.suchit.apigateway.ratelimit.entity.RateLimitConfig;

@Repository
public interface RateLimitRepository extends JpaRepository<RateLimitConfig, Long> {

	Optional<RateLimitConfig> findByApiPathAndRole(String apiPath, String role);
}