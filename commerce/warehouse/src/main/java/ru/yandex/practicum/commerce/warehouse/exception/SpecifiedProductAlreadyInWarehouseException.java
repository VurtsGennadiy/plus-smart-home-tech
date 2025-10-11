package ru.yandex.practicum.commerce.warehouse.exception;

/**
 * Исключение, возникающее при попытке добавить уже существующий продукт на склад
 */
public class SpecifiedProductAlreadyInWarehouseException extends RuntimeException {
    public SpecifiedProductAlreadyInWarehouseException(String message) {
        super(message);
    }
}
