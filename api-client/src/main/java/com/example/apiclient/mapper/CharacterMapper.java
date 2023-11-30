package com.example.apiclient.mapper;

import com.example.apiclient.dto.CharacterDTO;
import com.example.apiclient.dto.ThumbnailDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CharacterMapper {

    public static List<CharacterDTO> toDtoList(JsonNode rootNode) {
        ArrayNode resultsNode = getResultsNode(rootNode);

        List<CharacterDTO> characters = new ArrayList<>();

        resultsNode.elements().forEachRemaining(each -> {
            characters.add(CharacterMapper.toDto(each));
        });

        return characters;
    }

    private static CharacterDTO toDto(JsonNode characterNode) {
        if (Objects.isNull(characterNode)) {
            throw new IllegalArgumentException("El nodo character no puede ser null");
        }

        CharacterDTO dto = new CharacterDTO(
                Long.parseLong(characterNode.get("id").asText()),
                characterNode.get("name").asText(),
                characterNode.get("description").asText(),
                characterNode.get("modified").asText(),
                characterNode.get("resourceURI").asText()
        );

        return dto;
    }

    private static ArrayNode getResultsNode(JsonNode rootNode) {
        if (Objects.isNull(rootNode)) {
            throw new IllegalArgumentException("El nodo raíz no puede ser null");
        }

        JsonNode dataNode = rootNode.get("data");
        return (ArrayNode) dataNode.get("results");
    }

    public static List<CharacterDTO.CharacterInfoDTO> toInfoDtoList(JsonNode response) {
        ArrayNode resultsNode = getResultsNode(response);

        List<CharacterDTO.CharacterInfoDTO> characters = new ArrayList<>();

        resultsNode.elements().forEachRemaining(each -> {
            characters.add(CharacterMapper.toInfoDto(each));
        });

        return characters;
    }

    private static CharacterDTO.CharacterInfoDTO toInfoDto(JsonNode characterNode) {
        if (Objects.isNull(characterNode)) {
            throw new IllegalArgumentException("El nodo character no puede ser null");
        }

        JsonNode thumbnailNode = characterNode.get("thumbnail");

        ThumbnailDTO thumbnailDTO = new ThumbnailDTO(
                thumbnailNode.get("path").asText(),
                thumbnailNode.get("extension").asText()
        );

        String image = thumbnailDTO.path().concat(".").concat(thumbnailDTO.extension());

        CharacterDTO.CharacterInfoDTO dto = new CharacterDTO.CharacterInfoDTO(
                image,
                characterNode.get("description").asText()
        );

        return dto;
    }
}
