package com.example.apiclient.dto;

public record MyPageable(
        long offset,
        long limit
) {
}
