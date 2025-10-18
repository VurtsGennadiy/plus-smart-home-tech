package ru.yandex.practicum.commerce.shoppingstore.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.commerce.interaction.exception.ErrorResponse;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductNotFoundEx(ProductNotFoundException ex) {
        HttpStatus responseStatus = HttpStatus.NOT_FOUND;
        String userMessage = "Товар не найден";
        ErrorResponse errorResponse = new ErrorResponse(ex, responseStatus.value(), userMessage);
        log.error("Product not found: {}", ex.getMessage());
        return new ResponseEntity<>(errorResponse, responseStatus);
    }
}
