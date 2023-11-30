package com.example.api.security.validator;

import com.example.api.service.AuthenticationService;
import org.springframework.stereotype.Component;

@Component("interactionLogValidator")
public class UserInteractionLogValidator {

    private final AuthenticationService authenticationService;

    public UserInteractionLogValidator(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    public boolean validate(String username) {
        String userLoggedIn = authenticationService.getUserLoggedIn().getUsername();

        return userLoggedIn.equals(username);
    }
}
