package com.example.api.service;

import com.example.apiclient.dto.CharacterDTO;
import com.example.apiclient.dto.MyPageable;

import java.util.List;

public interface CharacterService {
    List<CharacterDTO> findAll(MyPageable pageable, String name, int[] comics, int[] series);

    CharacterDTO.CharacterInfoDTO findInforById(Long characterId);
}
