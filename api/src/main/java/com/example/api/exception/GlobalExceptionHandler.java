package com.example.api.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;

import java.nio.file.AccessDeniedException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorDTO> handlerGeneralExceptions(Exception exception,
                                                                HttpServletRequest request,
                                                                WebRequest webRequest) {
        if (exception instanceof HttpClientErrorException) {
            return this.handleHttpClientErrorException((HttpClientErrorException) exception, request, webRequest);
        } else if (exception instanceof AccessDeniedException) {
            return this.handleAccessDeniedException((AccessDeniedException) exception, request, webRequest);
        } else if (exception instanceof AuthenticationCredentialsNotFoundException) {
            return this.handleAuthenticationCredentialNotFoundException((AuthenticationCredentialsNotFoundException) exception, request, webRequest);
        } else {
            return this.handleGenericException(exception, request, webRequest);
        }
    }

    private ResponseEntity<ApiErrorDTO> handleGenericException(Exception exception,
                                                               HttpServletRequest request,
                                                               WebRequest webRequest) {
        ApiErrorDTO dto = new ApiErrorDTO(
                "Error inesperado... vuelva a intentarlo",
                exception.getMessage(),
                request.getMethod(),
                request.getRequestURL().toString()
        );

        return ResponseEntity.internalServerError().body(dto);

    }

    private ResponseEntity<ApiErrorDTO> handleAuthenticationCredentialNotFoundException(
                                        AuthenticationCredentialsNotFoundException exception,
                                        HttpServletRequest request,
                                        WebRequest webRequest) {

        ApiErrorDTO dto = new ApiErrorDTO(
                "No tienes acceso a este recurso",
                exception.getMessage(),
                request.getMethod(),
                request.getRequestURL().toString()
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(dto);
    }

    private ResponseEntity<ApiErrorDTO> handleAccessDeniedException(
                                            AccessDeniedException exception,
                                            HttpServletRequest request,
                                            WebRequest webRequest) {
        ApiErrorDTO dto = new ApiErrorDTO(
                "No tienes acceso a este recurso",
                exception.getMessage(),
                request.getMethod(),
                request.getRequestURL().toString()
        );

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(dto);
    }

    private ResponseEntity<ApiErrorDTO> handleHttpClientErrorException(
                                                            HttpClientErrorException exception,
                                                            HttpServletRequest request,
                                                            WebRequest webRequest) {

        String message;

        if (exception instanceof HttpClientErrorException.Forbidden) {
            message = "No tienes acceso a este recurso";
        } else if (exception instanceof HttpClientErrorException.Unauthorized) {
            message = "No has iniciado sessión para acceder a este recurso";
        } else if (exception instanceof HttpClientErrorException.NotFound) {
            message = "Recurso no encontrado";
        } else if (exception instanceof HttpClientErrorException.Conflict) {
            message = "Conflicto en el procesamiento de la petición";
        } else {
            message = "Error inesperado al realizar consulta";
        }

        ApiErrorDTO dto = new ApiErrorDTO(
                message,
                exception.getMessage(),
                request.getMethod(),
                request.getRequestURL().toString()
        );

        return ResponseEntity.status(exception.getStatusCode()).body(dto);
    }

}
