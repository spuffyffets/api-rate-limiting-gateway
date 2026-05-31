package com.suchit.auth.service;

import org.modelmapper.ModelMapper;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;

import com.suchit.auth.dto.Response;
import com.suchit.auth.dto.LoginRequest;
import com.suchit.auth.dto.RegisterRequest;
import com.suchit.auth.entity.Role;
import com.suchit.auth.entity.User;
import com.suchit.auth.exception.EmailAlreadyExistsException;
import com.suchit.auth.exception.InvalidCredentialsException;
import com.suchit.auth.exception.NotFoundException;
import com.suchit.auth.repository.UserRepository;
import com.suchit.auth.security.JwtUtils;

import lombok.RequiredArgsConstructor;



@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

	 private final UserRepository userRepository;
	    private final PasswordEncoder passwordEncoder;
	    private final ModelMapper modelMapper;
	    private final JwtUtils jwtUtils;


	    @Override
	    public Response register(RegisterRequest registerRequest) {

	        Role role = Role.MANAGER.ADMIN;

	        if (registerRequest.getRole() != null) {
	            role=registerRequest.getRole();
	        }

	        User userToSave = User.builder()
	                .name(registerRequest.getName())
	                .email(registerRequest.getEmail())
	                .password(passwordEncoder.encode(registerRequest.getPassword()))
	                .phoneNumber(registerRequest.getPhoneNumber())
	                .role(role)
	                .build();
	        
	        if(userRepository.existsByEmail(registerRequest.getEmail())) {
	        	throw new EmailAlreadyExistsException("Email already exists");
	        }
	        userRepository.save(userToSave);

	        return Response.builder()
	                .status(200)
	                .message("user created successfully")
	                .build();
	    }

	    @Override
	    public Response login(LoginRequest loginRequest) {
	       User user = userRepository.findByEmail(loginRequest.getEmail())
	               .orElseThrow(()-> new NotFoundException("Email not Found"));

	        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
	            throw new InvalidCredentialsException("password does not match");
	        }
//	        String token = jwtUtils.generateToken(user.getEmail());
	        String token = jwtUtils.generateToken(user);

	        return Response.builder()
	                .status(200)
	                .message("user logged in successfully")
	                .role(user.getRole())
	                .token(token)
	                .expirationTime("6 month")
	                .build();
	    }

}