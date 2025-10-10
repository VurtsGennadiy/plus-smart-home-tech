package ru.yandex.practicum.commerce.shoopingcart.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.commerce.interaction.exception.ErrorResponse;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(NoProductsInShoppingCartException.class)
    public ResponseEntity<ErrorResponse> handleNoProductsInShoppingCartException(NoProductsInShoppingCartException ex) {
        HttpStatus responseStatus = HttpStatus.BAD_REQUEST;
        ErrorResponse errorResponse = new ErrorResponse(ex, responseStatus.value(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, responseStatus);
    }

    @ExceptionHandler(NotAuthorizedUserException.class)
    public ResponseEntity<ErrorResponse> handleNotAuthorized(NotAuthorizedUserException ex) {
        HttpStatus responseStatus = HttpStatus.UNAUTHORIZED;
        ErrorResponse errorResponse = new ErrorResponse(ex, responseStatus.value(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, responseStatus);
    }
}
