package com.suchit.apigateway.ratelimit;

import java.time.Duration;

import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

@Service
public class RateLimitService {

    private final ReactiveStringRedisTemplate redisTemplate;

    public RateLimitService(
            ReactiveStringRedisTemplate redisTemplate) {

        this.redisTemplate = redisTemplate;
    }

//    public Mono<Boolean> isAllowed(String key) {
//
//        return redisTemplate.opsForValue()
//                .increment(key)
//                .flatMap(count -> {
//
//                    if (count == 1) {
//
//                        return redisTemplate
//                                .expire(key,
//                                        Duration.ofMinutes(1))
//                                .thenReturn(true);
//                    }
//
//                    return Mono.just(count <= 10);
//                });
//    }
    
    public Mono<Boolean> isAllowed(
            String key,
            int limit) {

        return redisTemplate.opsForValue()
                .increment(key)
                .flatMap(count -> {

                    if (count == 1) {

                        return redisTemplate
                                .expire(
                                        key,
                                        Duration.ofMinutes(1))
                                .thenReturn(true);
                    }

                    return Mono.just(count <= limit);
                });
    }
}