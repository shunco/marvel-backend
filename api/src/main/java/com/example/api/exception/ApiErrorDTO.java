package com.example.api.exception;

public record ApiErrorDTO(
        String message,
        String backendMessage,
        String method,
        String url

) {
}
