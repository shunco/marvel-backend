package com.example.api.service;

import com.example.api.dto.LoginRequest;
import com.example.api.dto.LoginResponse;
import com.example.api.persistence.entity.User;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AuthenticationService {

    private final HttpSecurity http;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    public AuthenticationService(HttpSecurity http, UserDetailsService userDetailsService, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.http = http;
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public LoginResponse authenticate(LoginRequest loginRequest) {
        UserDetails user = userDetailsService.loadUserByUsername(loginRequest.username());

        Authentication authentication =  new UsernamePasswordAuthenticationToken(
                user, loginRequest.password(), user.getAuthorities()
        );
        authenticationManager.authenticate(authentication);

        String jwt = jwtService.generateToken(user, generateExtraClaims(user));

        return new LoginResponse(jwt, user.getUsername());
    }

    private Map<String, Object> generateExtraClaims(UserDetails user) {
        Map<String, Object> extraClaims = new HashMap<>();

        String roleName = ((User) user).getRole().getName().name();

        extraClaims.put("role", roleName);
        extraClaims.put("authorities", user.getAuthorities().stream()
                                           .map(GrantedAuthority::getAuthority)
                                           .collect(Collectors.toList()));

        return extraClaims;
    }

    public void logout() {
        try {
            http.logout(logoutConfig -> logoutConfig.deleteCookies("JSESSIONID")
                    .clearAuthentication(true)
                    .invalidateHttpSession(true));
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }

    }

    public UserDetails getUserLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof UsernamePasswordAuthenticationToken authToken)) {
            throw new AuthenticationCredentialsNotFoundException("Se requiere autenticación completa");
        }

        return (UserDetails) authToken.getPrincipal();
    }
}
