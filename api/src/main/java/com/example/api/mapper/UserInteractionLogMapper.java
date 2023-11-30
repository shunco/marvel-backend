package com.example.api.mapper;

import com.example.api.dto.GetUserInteractionDTO;
import com.example.api.persistence.entity.UserInteractionLog;

import java.util.Objects;

public class UserInteractionLogMapper {

    public static GetUserInteractionDTO toDto(UserInteractionLog entity) {
        if (Objects.isNull(entity)) return null;

        return new GetUserInteractionDTO(
                entity.getId(),
                entity.getUrl(),
                entity.getHttpMethod(),
                entity.getUsername(),
                entity.getTimestamp(),
                entity.getRemoteAddress()
        );
    }
}
