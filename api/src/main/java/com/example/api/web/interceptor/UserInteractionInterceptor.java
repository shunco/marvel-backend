package com.example.api.web.interceptor;

import com.example.api.persistence.entity.UserInteractionLog;
import com.example.api.persistence.repository.UserInteractionLogRepository;
import com.example.api.service.AuthenticationService;
import com.example.apiclient.exception.ApiErrorException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDateTime;

@Component
public class UserInteractionInterceptor implements HandlerInterceptor {
    private final UserInteractionLogRepository userInteractionLogRepository;
    private final AuthenticationService authenticationService;

    public UserInteractionInterceptor(UserInteractionLogRepository userInteractionLogRepository,
                                      @Lazy AuthenticationService authenticationService) {
        this.userInteractionLogRepository = userInteractionLogRepository;
        this.authenticationService = authenticationService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (request.getRequestURI().startsWith("/characters")) {
            UserInteractionLog userLog = new UserInteractionLog();
            userLog.setHttpMethod(request.getMethod());
            userLog.setUrl(request.getRequestURL().toString());
            userLog.setRemoteAddress(request.getRemoteAddr());
            userLog.setTimestamp(LocalDateTime.now());

            UserDetails user = authenticationService.getUserLoggedIn();
            userLog.setUsername(user.getUsername());

            try {
                userInteractionLogRepository.save(userLog);
                return true;
            } catch (Exception exception) {
                throw new ApiErrorException("No se logró guardar el registro de interacción correctamente");
            }
        }

        return true;
    }
}
