package com.example.api.service.impl;

import com.example.api.service.CharacterService;
import com.example.apiclient.dto.CharacterDTO;
import com.example.apiclient.dto.MyPageable;
import com.example.apiclient.repository.CharacterRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CharacterServiceImpl implements CharacterService {

    private final CharacterRepository characterRepository;

    public CharacterServiceImpl(CharacterRepository characterRepository) {
        this.characterRepository = characterRepository;
    }

    @Override
    public List<CharacterDTO> findAll(MyPageable pageable, String name, int[] comics, int[] series) {
        return characterRepository.findAll(pageable, name, comics, series);
    }

    @Override
    public CharacterDTO.CharacterInfoDTO findInforById(Long characterId) {
        return characterRepository.findInfoById(characterId);
    }
}
