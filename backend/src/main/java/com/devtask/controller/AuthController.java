package com.devtask.controller;
import com.devtask.dto.*;
import com.devtask.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RestController @RequestMapping("/api/auth")
public class AuthController {
    private final AuthService service;
    public AuthController(AuthService service){this.service=service;}
    @PostMapping("/register") public AuthResponse register(@Valid @RequestBody RegisterRequest r){return service.register(r);}
    @PostMapping("/login") public AuthResponse login(@Valid @RequestBody LoginRequest r){return service.login(r);}
    @GetMapping("/me") public UserResponse current(@AuthenticationPrincipal String email){return service.currentUser(email);}
}
