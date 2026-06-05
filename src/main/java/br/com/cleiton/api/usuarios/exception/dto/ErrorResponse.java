package br.com.cleiton.api.usuarios.exception.dto;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponse(
        LocalDateTime time,
        int status,
        String message,
        List<String> errors
) {

    public static ErrorResponse of(HttpStatus status, String message) {
        return new ErrorResponse(LocalDateTime.now(), status.value(), message, List.of());
    }

    public static ErrorResponse of(HttpStatus status, String message, List<String> errors) {
        return new ErrorResponse(LocalDateTime.now(), status.value(), message, errors);
    }

}
