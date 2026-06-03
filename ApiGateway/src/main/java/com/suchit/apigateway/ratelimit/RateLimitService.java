package com.suchit.apigateway.ratelimit;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Duration;

import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

@Service
public class RateLimitService {

	private final ReactiveStringRedisTemplate redisTemplate;
	private final ObjectMapper objectMapper;

	public RateLimitService(ReactiveStringRedisTemplate redisTemplate, ObjectMapper objectMapper) {

		this.redisTemplate = redisTemplate;
		this.objectMapper = objectMapper;
	}

	public Mono<Boolean> isAllowed(String key, int capacity, int refillRate) {

		return getBucket(key, capacity).flatMap(bucket -> {

			bucket = refillBucket(bucket, capacity, refillRate);

			if (bucket.getTokens() <= 0) {

				return Mono.just(false);
			}
//			System.out.println("Tokens Remaining = " + bucket.getTokens());

			bucket.setTokens(bucket.getTokens() - 1);

			try {

				String bucketJson = objectMapper.writeValueAsString(bucket);

				return redisTemplate.opsForValue().set(key, bucketJson)
						.then(redisTemplate.expire(key, Duration.ofHours(1))).thenReturn(true);

			} catch (Exception e) {

				return Mono.error(e);
			}
		});
	}

	private Mono<TokenBucket> getBucket(String key, int capacity) {

		return redisTemplate.opsForValue().get(key).flatMap(value -> {

			try {

				TokenBucket bucket = objectMapper.readValue(value, TokenBucket.class);

				return Mono.just(bucket);

			} catch (Exception e) {

				return Mono.error(e);
			}
		}).switchIfEmpty(

				Mono.just(TokenBucket.builder().tokens(capacity).lastRefillTime(System.currentTimeMillis()).build()));
	}

	private TokenBucket refillBucket(TokenBucket bucket, int capacity, int refillRate) {

		long currentTime = System.currentTimeMillis();

		long elapsedSeconds = (currentTime - bucket.getLastRefillTime()) / 1000;

		if (elapsedSeconds <= 0) {

			return bucket;
		}

		int tokensToAdd = (int) (elapsedSeconds * refillRate);

		int newTokenCount = Math.min(capacity, bucket.getTokens() + tokensToAdd);

		bucket.setTokens(newTokenCount);

		// Update timestamp only when refill actually happens
		bucket.setLastRefillTime(currentTime);

		return bucket;
	}
}