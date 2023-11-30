package com.example.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record GetUserInteractionDTO(
        Long id,
        String url,
        String httpMethod,
        String username,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
        LocalDateTime timestamp,
        String remoteAddress
) {
}
