package ru.yandex.practicum.commerce.warehouse.exception;

/**
 * Исключение, возникающее при попытке забронировать товар на складе, которого нет в достаточном количестве
 */
public class ProductInShoppingCartLowQuantityInWarehouse extends RuntimeException {
    public ProductInShoppingCartLowQuantityInWarehouse(String message) {
        super(message);
    }
}
