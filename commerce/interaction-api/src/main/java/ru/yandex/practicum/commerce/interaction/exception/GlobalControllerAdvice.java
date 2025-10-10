package ru.yandex.practicum.commerce.interaction.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
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
        return new ResponseEntity<>(errorResponse, responseStatus);
    }
}
