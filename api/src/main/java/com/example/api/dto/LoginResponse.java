package com.example.api.dto;

public record LoginResponse(
        String jwt,
        String username
) {
}
