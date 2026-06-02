package com.suchit.apigateway.ratelimit;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenBucket {

    private Integer tokens;

    private Long lastRefillTime;
}