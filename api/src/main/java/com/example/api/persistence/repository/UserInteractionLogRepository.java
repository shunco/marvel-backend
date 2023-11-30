package com.example.api.persistence.repository;

import com.example.api.persistence.entity.UserInteractionLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInteractionLogRepository extends JpaRepository<UserInteractionLog, Long> {
    Page<UserInteractionLog> findByUsername(Pageable pageable, String username);
}
