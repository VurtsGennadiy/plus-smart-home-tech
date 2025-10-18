package ru.yandex.practicum.commerce.warehouse.exception;

/**
 * Исключение, возникающее при попытке принять увеличить остаток товара на складе, для которого отсутствует описание в складе.
 */
public class NoSpecifiedProductInWarehouseException extends RuntimeException {
    public NoSpecifiedProductInWarehouseException(String message) {
        super(message);
    }
}
