package com.devtask.dto;
import jakarta.validation.constraints.*;
public record RegisterRequest(@Email @NotBlank String email, @NotBlank @Size(min=8, max=72) String password) {}
