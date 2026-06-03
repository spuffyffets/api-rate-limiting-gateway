package com.suchit.apigateway.ratelimit.service;

import com.suchit.apigateway.ratelimit.repository.RateLimitRepository;

import org.springframework.stereotype.Service;

import com.suchit.apigateway.ratelimit.dto.RateLimitPolicy;
import com.suchit.apigateway.ratelimit.dto.RateLimitRequest;
import com.suchit.apigateway.ratelimit.dto.Response;
import com.suchit.apigateway.ratelimit.entity.*;
import com.suchit.apigateway.ratelimit.service.RateLimitConfigService;

@Service
public class RateLimitConfigServiceImpl implements RateLimitConfigService {

	private final RateLimitRepository rateLimitRepository;

	public RateLimitConfigServiceImpl(RateLimitRepository rateLimitRepository) {

		this.rateLimitRepository = rateLimitRepository;
	}

	@Override
	public RateLimitPolicy getPolicy(String apiPath, String role) {

		RateLimitConfig config = rateLimitRepository.findByApiPathAndRole(apiPath, role).orElse(null);

		if (config == null) {

			return RateLimitPolicy.builder().bucketCapacity(10).refillRate(1).build();
		}
//		System.out.println(config.getBucketCapacity());
//		System.out.println(config.getRefillRate());
		return RateLimitPolicy.builder().bucketCapacity(config.getBucketCapacity()).refillRate(config.getRefillRate())
				.build();
	}

	@Override
	public Response addRateLimit(RateLimitRequest request) {

		RateLimitConfig config = RateLimitConfig.builder().apiPath(request.getApiPath()).role(request.getRole())
				.bucketCapacity(request.getBucketCapacity()).refillRate(request.getRefillRate()).build();

		rateLimitRepository.save(config);

		return Response.builder().status(200).message("Rate Limit Added Successfully").build();
	}

	@Override
	public Response getAllRateLimits() {

		return Response.builder().status(200).message("Rate Limits Fetched Successfully")
				.data(rateLimitRepository.findAll()).build();
	}

	@Override
	public Response updateRateLimit(Long id, RateLimitRequest request) {

		RateLimitConfig config = rateLimitRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Rate Limit Not Found"));

		config.setApiPath(request.getApiPath());

		config.setRole(request.getRole());

		config.setBucketCapacity(request.getBucketCapacity());

		config.setRefillRate(request.getRefillRate());

		rateLimitRepository.save(config);

		return Response.builder().status(200).message("Rate Limit Updated Successfully").build();
	}

	@Override
	public Response deleteRateLimit(Long id) {

		RateLimitConfig config = rateLimitRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Rate Limit Not Found"));

		rateLimitRepository.delete(config);

		return Response.builder().status(200).message("Rate Limit Deleted Successfully").build();
	}

}
