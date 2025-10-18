package ru.yandex.practicum.commerce.interaction.exception;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Глобальный обработчик ошибок, общих для всех микросервисов
 */

@RestControllerAdvice
@Slf4j
public class GlobalControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidError(MethodArgumentNotValidException ex) {
        HttpStatus responseStatus = HttpStatus.BAD_REQUEST;

        StringBuilder userMessage = new StringBuilder();
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String field = error.getField();
            String message = error.getDefaultMessage();
            errors.put(field, message);
        });

        errors.forEach((field, message) -> {
            userMessage.append("Поле ");
            userMessage.append(field);
            userMessage.append(" ");
            userMessage.append(message);
            userMessage.append(". ");
        });

        ErrorResponse errorResponse = new ErrorResponse(ex, responseStatus.value(), userMessage.toString().strip());
        log.warn("Invalid request, validation exception: {}", ex.getMessage());
        return new ResponseEntity<>(errorResponse, responseStatus);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        HttpStatus responseStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        ErrorResponse errorResponse = new ErrorResponse(ex, responseStatus.value(), ex.getMessage());
        log.error("Error:", ex);
        return new ResponseEntity<>(errorResponse, responseStatus);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleFeignException(FeignException ex) {
        HttpStatus responseStatus = HttpStatus.valueOf(ex.status());
        log.error("Error executing request from feign client:", ex);
        return ResponseEntity
                .status(responseStatus)
                .header("Content-Type", "application/json")
                .body(ex.contentUTF8());
    }
}
