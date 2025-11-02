package ru.yandex.practicum.commerce.payment.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.commerce.interaction.exception.ErrorResponse;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {
    @ExceptionHandler(NotEnoughInfoInOrderToCalculateException.class)
    public ResponseEntity<ErrorResponse> handleNotEnoughInfoInOrderToCalculateEx(NotEnoughInfoInOrderToCalculateException ex) {
        HttpStatus responseStatus = HttpStatus.BAD_REQUEST;
        ErrorResponse errorResponse = new ErrorResponse(ex, responseStatus.value(), ex.getMessage());
        log.error("Calculate total error: {}", ex.getMessage());
        return new ResponseEntity<>(errorResponse, responseStatus);
    }
}
