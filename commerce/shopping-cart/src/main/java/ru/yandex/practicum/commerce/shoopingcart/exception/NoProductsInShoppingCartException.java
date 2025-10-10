package ru.yandex.practicum.commerce.shoopingcart.exception;

/**
 * Исключение, которое возникает при попытке выполнить операцию над пустой корзиной покупок.
 */
public class NoProductsInShoppingCartException extends RuntimeException {
    public NoProductsInShoppingCartException(String message) {
        super(message);
    }
}
