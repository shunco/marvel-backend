package com.example.apiclient.dto;

public record CharacterDTO (
        Long id,
        String name,
        String description,
        String modified,
        String resourceURI
) {

    public static record CharacterInfoDTO (
            String imagePath,
            String description
    ) {
    }
}
