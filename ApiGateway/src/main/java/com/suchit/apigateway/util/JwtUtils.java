package com.suchit.apigateway.util;

import java.nio.charset.StandardCharsets;
import java.util.function.Function;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;

@Component
public class JwtUtils {

    private SecretKey key;

    @Value("${secreteJwtString}")
    private String secreteJwtString;

    @PostConstruct
    public void init() {

        byte[] keyBytes =
                secreteJwtString.getBytes(StandardCharsets.UTF_8);

        key = new SecretKeySpec(
                keyBytes,
                "HmacSHA256");
    }

    public String getUsernameFromToken(String token) {

        return extractClaims(
                token,
                Claims::getSubject);
    }

    public String getRoleFromToken(String token) {

        return extractClaims(
                token,
                claims -> claims.get("role", String.class));
    }

    public boolean isTokenValid(String token) {

        try {

            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);

            return true;

        } catch (Exception e) {

            return false;
        }
    }

    private <T> T extractClaims(
            String token,
            Function<Claims, T> resolver) {

        Claims claims =
                Jwts.parser()
                        .verifyWith(key)
                        .build()
                        .parseSignedClaims(token)
                        .getPayload();

        return resolver.apply(claims);
    }
}