package com.example.apiclient.repository;

import com.example.apiclient.MarvelAPIConfig;
import com.example.apiclient.dto.CharacterDTO;
import com.example.apiclient.dto.MyPageable;
import com.example.apiclient.mapper.CharacterMapper;
import com.example.apiclient.service.HttpClientService;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.IntStream;

@Repository
public class CharacterRepository {

    private final MarvelAPIConfig marvelAPIConfig;
    private final HttpClientService httpClientService;

    @Value("${marvel.base-path}")
    private String basePath;
    private String characterPath;

    public CharacterRepository(MarvelAPIConfig marvelAPIConfig, HttpClientService httpClientService) {
        this.marvelAPIConfig = marvelAPIConfig;
        this.httpClientService = httpClientService;
    }

    @PostConstruct
    private void setPath() {
        characterPath = basePath.concat("/").concat("characters");
    }

    public List<CharacterDTO> findAll(MyPageable pageable, String name, int[] comics, int[] series) {
        Map<String, String> marvelQueryParams = getQueryParamsForFindAll(pageable, name, comics, series);

        JsonNode response = httpClientService.doGet(characterPath, marvelQueryParams, JsonNode.class);

        return CharacterMapper.toDtoList(response);
    }

    private Map<String, String> getQueryParamsForFindAll(MyPageable pageable, String name, int[] comics, int[] series) {
        Map<String, String> marvelQueryParams = marvelAPIConfig.getAuthenticationQueryParams();
        marvelQueryParams.put("offset", Long.toString(pageable.offset()));
        marvelQueryParams.put("limit", Long.toString(pageable.limit()));

        if (StringUtils.hasText(name)) {
            marvelQueryParams.put("name", name);
        }

        if (Objects.nonNull(comics)) {
            String comicsAsString = joinIntArray(comics);
            marvelQueryParams.put("comics", comicsAsString);
        }

        if (Objects.nonNull(series)) {
            String seriesAsString = joinIntArray(series);
            marvelQueryParams.put("comics", seriesAsString);
        }

        return marvelQueryParams;
    }

    private String joinIntArray(int[] intArray) {
        List<String> stringArray = IntStream.of(intArray).boxed()
                .map(Object::toString)
                .toList();
        return String.join(",", stringArray);
    }

    public CharacterDTO.CharacterInfoDTO findInfoById(Long characterId) {
        Map<String, String> marvelQueryParams = marvelAPIConfig.getAuthenticationQueryParams();
        String finalURL = characterPath.concat("/").concat(Long.toString(characterId));

        JsonNode response = httpClientService.doGet(finalURL, marvelQueryParams, JsonNode.class);

        return CharacterMapper.toInfoDtoList(response).get(0);
    }
}