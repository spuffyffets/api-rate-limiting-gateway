package com.suchit.auth.service;

import com.suchit.auth.dto.Response;
import com.suchit.auth.dto.LoginRequest;
import com.suchit.auth.dto.RegisterRequest;

public interface AuthService {

    Response register(RegisterRequest request);

    Response login(LoginRequest request);
}