package ru.yandex.practicum.commerce.warehouse.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.commerce.interaction.exception.ErrorResponse;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {
    @ExceptionHandler(SpecifiedProductAlreadyInWarehouseException.class)
    public ResponseEntity<ErrorResponse> handleSpecifiedProductAlreadyInWarehouseEx(SpecifiedProductAlreadyInWarehouseException ex) {
        HttpStatus responseStatus = HttpStatus.BAD_REQUEST;
        String userMessage = "Ошибка, описание для этого товара уже зарегистрировано на складе";
        ErrorResponse errorResponse = new ErrorResponse(ex, responseStatus.value(), userMessage);
        log.warn("Description of product has not been added: {}", ex.getMessage());
        return new ResponseEntity<>(errorResponse, responseStatus);
    }

    @ExceptionHandler(NoSpecifiedProductInWarehouseException.class)
    public ResponseEntity<ErrorResponse> handleNoSpecifiedProductInWarehouseEx(NoSpecifiedProductInWarehouseException ex) {
        HttpStatus responseStatus = HttpStatus.BAD_REQUEST;
        String userMessage = "Ошибка, для этого товара отсутствует описание в складской системе";
        ErrorResponse errorResponse = new ErrorResponse(ex, responseStatus.value(), userMessage);
        log.warn("Unable to receive product into warehouse: {}", ex.getMessage());
        return new ResponseEntity<>(errorResponse, responseStatus);
    }

    @ExceptionHandler(ProductInShoppingCartLowQuantityInWarehouse.class)
    public ResponseEntity<ErrorResponse> handleProductInShoppingCartLowQuantityInWarehouseEx(ProductInShoppingCartLowQuantityInWarehouse ex) {
        HttpStatus responseStatus = HttpStatus.BAD_REQUEST;
        String userMessage = ex.getMessage();
        ErrorResponse errorResponse = new ErrorResponse(ex, responseStatus.value(), userMessage);
        log.error("Insufficient quantity of product in warehouse. {}", ex.getMessage());
        return new ResponseEntity<>(errorResponse, responseStatus);
    }
}
