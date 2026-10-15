package com.devtask.service;

import com.devtask.dto.*;
import com.devtask.model.*;
import com.devtask.exception.ResourceNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import com.devtask.dto.UserResponse;
import com.devtask.repository.UserRepository;
import com.devtask.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository users; private final PasswordEncoder encoder; private final JwtService jwt;

    public AuthService(UserRepository users, PasswordEncoder encoder, JwtService jwt){
        this.users=users; this.encoder=encoder; this.jwt=jwt;
    }

    public AuthResponse register(RegisterRequest r){
        String email=r.email().trim().toLowerCase();
        if(users.existsByEmail(email)) throw new IllegalArgumentException("Email already exists");
        User u=new User(); u.setEmail(email); u.setPassword(encoder.encode(r.password())); u.setRole(Role.DEVELOPER);
        users.save(u);
        return new AuthResponse(jwt.generate(u.getEmail(),u.getRole().name()),u.getEmail(),u.getRole().name());
    }

    public AuthResponse login(LoginRequest r){
        User u=users.findByEmail(r.email().trim().toLowerCase()).orElseThrow(()->new BadCredentialsException("Invalid credentials"));
        if(!encoder.matches(r.password(),u.getPassword())) throw new BadCredentialsException("Invalid credentials");
        return new AuthResponse(jwt.generate(u.getEmail(),u.getRole().name()),u.getEmail(),u.getRole().name());
    }

    public UserResponse currentUser(String email){
        User u=users.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("User not found"));
        return new UserResponse(u.getId(),u.getEmail(),u.getRole().name());
    }
}
