package com.example.api.service;

import com.example.api.dto.GetUserInteractionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserLogInteractionService {
    Page<GetUserInteractionDTO> findAll(Pageable pageable);

    Page<GetUserInteractionDTO> findByUsername(Pageable pageable, String username);
}
