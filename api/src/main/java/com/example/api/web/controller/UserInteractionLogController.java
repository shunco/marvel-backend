package com.example.api.web.controller;

import com.example.api.dto.GetUserInteractionDTO;
import com.example.api.service.UserLogInteractionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user-interactions")
public class UserInteractionLogController {

    private final UserLogInteractionService userLogInteractionService;

    public UserInteractionLogController(UserLogInteractionService userLogInteractionService) {
        this.userLogInteractionService = userLogInteractionService;
    }

    @PreAuthorize("hasAuthority('user-interaction:read-all')")
    @GetMapping
    public ResponseEntity<Page<GetUserInteractionDTO>> findAll(
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "10") int limit
    ) {

        Pageable pageable = buildPageable(offset, limit);

        return ResponseEntity.ok(userLogInteractionService.findAll(pageable));

    }

    private static Pageable buildPageable(int offset, int limit) {
        Pageable pageable;

        if (offset < 0) {
            throw new IllegalArgumentException("El atributo offset no puede ser menor a 0");
        }

        if (limit <= 0) {
            throw new IllegalArgumentException("El atributo limit no puede ser menor o igual a 0");
        }

        if (offset == 0) pageable = PageRequest.of(0, limit);
        else pageable = PageRequest.of(offset/limit, limit);

        return pageable;
    }

    @PreAuthorize("hasAuthority('user-interaction:read-by-username') || @interactionLogValidator.validate(#username)")
    @GetMapping("/{username}")
    public ResponseEntity<Page<GetUserInteractionDTO>> findByUsername(
            @PathVariable String username,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "10") int limit) {

        Pageable pageable = buildPageable(offset, limit);

        return ResponseEntity.ok(userLogInteractionService.findByUsername(pageable, username));

    }
}
