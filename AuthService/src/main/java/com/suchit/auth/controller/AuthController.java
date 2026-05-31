package com.suchit.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.suchit.auth.dto.Response;
import com.suchit.auth.dto.LoginRequest;
import com.suchit.auth.dto.RegisterRequest;
import com.suchit.auth.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public Response register(
            @RequestBody RegisterRequest request) {

        return authService.register(request);
    }

    @PostMapping("/login")
    public Response login(
            @RequestBody LoginRequest request) {

        return authService.login(request);
    }
    
    @GetMapping("/profile")
    public String profile() {
        return "Authenticated User";
    }
}