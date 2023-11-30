package com.example.api.service.impl;

import com.example.api.dto.GetUserInteractionDTO;
import com.example.api.mapper.UserInteractionLogMapper;
import com.example.api.persistence.repository.UserInteractionLogRepository;
import com.example.api.service.UserLogInteractionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserLogInteractionServiceImpl implements UserLogInteractionService {
    private final UserInteractionLogRepository userInteractionLogRepository;

    public UserLogInteractionServiceImpl(UserInteractionLogRepository userInteractionLogRepository) {
        this.userInteractionLogRepository = userInteractionLogRepository;
    }

    @Override
    public Page<GetUserInteractionDTO> findAll(Pageable pageable) {
        return userInteractionLogRepository.findAll(pageable)
                .map(UserInteractionLogMapper::toDto);
    }

    @Override
    public Page<GetUserInteractionDTO> findByUsername(Pageable pageable, String username) {
        return userInteractionLogRepository.findByUsername(pageable, username)
                .map(UserInteractionLogMapper::toDto);
    }
}
